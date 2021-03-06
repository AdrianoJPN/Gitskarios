package com.alorma.github.ui.activity;

import android.accounts.Account;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.alorma.github.BuildConfig;
import com.alorma.github.R;
import com.alorma.github.account.BaseAccountsFragmentManager;
import com.alorma.github.ui.ErrorHandler;
import com.alorma.github.ui.utils.DrawerImage;
import com.alorma.github.utils.AttributesUtils;
import com.alorma.gitskarios.core.client.BaseClient;
import com.alorma.gitskarios.core.client.StoreCredentials;
import com.alorma.github.sdk.bean.dto.response.Notification;
import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.login.AccountsHelper;
import com.alorma.github.sdk.services.notifications.GetNotificationsClient;
import com.alorma.github.sdk.utils.GitskariosSettings;
import com.alorma.github.ui.activity.base.BaseActivity;
import com.alorma.github.ui.activity.gists.GistsMainActivity;
import com.alorma.github.ui.fragment.ChangelogDialogSupport;
import com.alorma.github.ui.fragment.donate.DonateFragment;
import com.alorma.github.ui.fragment.events.EventsListFragment;
import com.alorma.github.ui.fragment.menu.OnMenuItemSelectedListener;
import com.alorma.github.ui.fragment.repos.GeneralReposFragment;
import com.alorma.github.ui.view.GitskariosProfileDrawerItem;
import com.alorma.github.ui.view.SecondarySwitchDrawerItem;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements OnMenuItemSelectedListener, AccountHeader.OnAccountHeaderListener {

    private static final int PROFILE_REQUEST_CODE = 555;
    private GeneralReposFragment reposFragment;
    private EventsListFragment eventsFragment;

    private Account selectedAccount;
    private Fragment lastUsedFragment;
    private Drawer resultDrawer;
    private ChangelogDialogSupport dialog;
    private int notificationsSizeCount = 0;
    private DonateFragment donateFragment;
    private SecondarySwitchDrawerItem notificationsDrawerItem;
    private List<Account> accountList;

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donateFragment = new DonateFragment();

        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().beginTransaction().add(donateFragment, "donate").commit();
        }

        setContentView(R.layout.generic_toolbar);
    }

    private boolean checkChangeLog() {
        if (getSupportFragmentManager() != null) {
            int currentVersion = BuildConfig.VERSION_CODE;
            GitskariosSettings settings = new GitskariosSettings(this);
            int version = settings.getVersion(0);

            if (currentVersion > version) {
                settings.saveVersion(currentVersion);
                dialog = ChangelogDialogSupport.create();
                dialog.show(getSupportFragmentManager(), "changelog");
            }
        }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        accountList = getAccounts();

        if (!accountList.isEmpty()) {
            selectedAccount = accountList.get(0);
            selectAccount(selectedAccount);
            createDrawer();
            onUserEventsSelected();
        }
    }

    private String getUserExtraName(Account account) {
        String accountName = account.name;
        String userMail = AccountsHelper.getUserMail(this, account);
        String userName = AccountsHelper.getUserName(this, account);
        if (!TextUtils.isEmpty(userMail)) {
            return userMail;
        } else if (!TextUtils.isEmpty(userName)) {
            return userName;
        }
        return accountName;
    }

    private void createDrawer() {
        int iconColor = ContextCompat.getColor(this, R.color.icons);

        AccountHeader accountHeader = buildHeader();
        //Now create your drawer and pass the AccountHeader.Result
        DrawerBuilder drawer = new DrawerBuilder();
        drawer.withActivity(this);
        drawer.withToolbar(getToolbar());
        drawer.withAccountHeader(accountHeader);
        OnCheckedChangeListener notificationsCheckedListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
                if (iDrawerItem != null && iDrawerItem.getIdentifier() == R.id.nav_drawer_notifications) {
                    changeNotificationState(selectedAccount, b);
                }

                if (!b) {
                    NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }
            }
        };
        notificationsDrawerItem = new SecondarySwitchDrawerItem().withName(R.string.menu_enable_notifications)
            .withDescription(R.string.menu_enable_notifications_description)
            .withIdentifier(R.id.nav_drawer_notifications)
            .withCheckable(false)
            .withOnCheckedChangeListener(notificationsCheckedListener)
            .withSelectable(false)
            .withIcon(Octicons.Icon.oct_bell)
            .withIconColor(iconColor);
        drawer.addDrawerItems(new PrimaryDrawerItem().withName(R.string.menu_events)
                .withIcon(Octicons.Icon.oct_calendar)
                .withIconColor(iconColor)
                .withIdentifier(R.id.nav_drawer_events),
            new PrimaryDrawerItem().withName(R.string.navigation_general_repositories)
                .withIcon(Octicons.Icon.oct_repo)
                .withIconColor(iconColor)
                .withIdentifier(R.id.nav_drawer_repositories), new PrimaryDrawerItem().withName(R.string.navigation_people)
                .withIcon(Octicons.Icon.oct_person)
                .withIconColor(iconColor)
                .withIdentifier(R.id.nav_drawer_people), new PrimaryDrawerItem().withName(R.string.navigation_gists)
                .withIcon(Octicons.Icon.oct_gist)
                .withIconColor(iconColor)
                .withIdentifier(R.id.nav_drawer_gists)
                .withSelectable(false), new DividerDrawerItem(), notificationsDrawerItem,
            new SecondaryDrawerItem().withName(R.string.navigation_settings)
                .withIcon(Octicons.Icon.oct_gear)
                .withIconColor(iconColor)
                .withIdentifier(R.id.nav_drawer_settings)
                .withSelectable(false), new DividerDrawerItem());

        if (donateFragment.enabled()) {
            SecondaryDrawerItem donateItem = new SecondaryDrawerItem().withName(R.string.support_development)
                .withIcon(Octicons.Icon.oct_heart)
                .withIconColor(iconColor)
                .withIdentifier(R.id.nav_drawer_support_development)
                .withSelectable(false);

            drawer.addDrawerItems(donateItem);
        }

        drawer.addDrawerItems(new SecondaryDrawerItem().withName(R.string.navigation_about)
            .withIcon(Octicons.Icon.oct_octoface)
            .withIconColor(iconColor)
            .withIdentifier(R.id.nav_drawer_about)
            .withSelectable(false), new SecondaryDrawerItem().withName(R.string.navigation_sign_out)
            .withIcon(Octicons.Icon.oct_sign_out)
            .withIconColor(iconColor)
            .withIdentifier(R.id.nav_drawer_sign_out)
            .withSelectable(false));

        drawer.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (drawerItem != null) {
                    int identifier = drawerItem.getIdentifier();
                    switch (identifier) {
                        case R.id.nav_drawer_events:
                            onUserEventsSelected();
                            break;
                        case R.id.nav_drawer_repositories:
                            onReposSelected();
                            break;
                        case R.id.nav_drawer_people:
                            onPeopleSelected();
                            break;
                        case R.id.nav_drawer_gists:
                            onGistsSelected();
                            break;
                        case R.id.nav_drawer_notifications:
                            openNotifications();
                            break;
                        case R.id.nav_drawer_settings:
                            onSettingsSelected();
                            break;
                        case R.id.nav_drawer_about:
                            onAboutSelected();
                            break;
                        case R.id.nav_drawer_sign_out:
                            signOut();
                            break;
                        case R.id.nav_drawer_support_development:
                            if (donateFragment != null && donateFragment.enabled()) {
                                donateFragment.launchDonate();
                            }
                            break;
                    }
                }

                return false;
            }
        });
        resultDrawer = drawer.build();
        resultDrawer.setSelection(R.id.nav_drawer_events);
    }

    private AccountHeader buildHeader() {

        // Create the AccountHeader

        DrawerImageLoader.init(new DrawerImage());

        AccountHeaderBuilder headerBuilder =
            new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.color.accent_welcome);

        headerBuilder.withOnAccountHeaderListener(this);

        boolean usedSelected = false;
        if (accountList != null) {
            for (Account account : accountList) {
                String userAvatar = AccountsHelper.getUserAvatar(this, account);
                ProfileDrawerItem profileDrawerItem = new GitskariosProfileDrawerItem().withName(account.name)
                    .withEmail(getUserExtraName(account))
                    .withIdentifier(account.hashCode());
                if (!TextUtils.isEmpty(userAvatar)) {
                    profileDrawerItem.withIcon(userAvatar);
                }
                profileDrawerItem.withSetSelected(!usedSelected);
                usedSelected = true;
                headerBuilder.addProfiles(profileDrawerItem);
            }
        }

        ProfileSettingDrawerItem itemAdd = new ProfileSettingDrawerItem()
            .withName(getString(R.string.add_account))
            .withDescription(getString(R.string.add_account_description))
            .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add)
                .actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text))
            .withIdentifier(1101);

        headerBuilder.addProfiles(itemAdd);

        return headerBuilder.build();
    }

    @Override
    public boolean onProfileChanged(View view, IProfile iProfile, boolean current) {
        if (iProfile.getIdentifier() != 1101) {
            if (current) {
                User user = new User();
                user.login = iProfile.getName().getText();
                Intent launcherIntent = ProfileActivity.createLauncherIntent(MainActivity.this, selectedAccount);
                startActivityForResult(launcherIntent, PROFILE_REQUEST_CODE);
            } else {
                String accountName = iProfile.getName().getText();
                for (Account account : accountList) {
                    if (account.name.equalsIgnoreCase(accountName)) {
                        selectAccount(account);
                        onUserEventsSelected();
                        break;
                    }
                }
            }
            return false;
        } else {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
    }

    private void selectAccount(final Account account) {
        boolean changingUser = selectedAccount != null && !selectedAccount.name.equals(account.name);
        this.selectedAccount = account;

        if (notificationsDrawerItem != null) {
            notificationsDrawerItem.withChecked(
                selectedAccount != null && ContentResolver.getSyncAutomatically(selectedAccount, selectedAccount.type));
            if (resultDrawer != null) {
                resultDrawer.updateItem(notificationsDrawerItem);
            }
        }

        StoreCredentials credentials = new StoreCredentials(MainActivity.this);
        credentials.clear();
        String authToken = AccountsHelper.getUserToken(this, account);

        credentials.storeToken(authToken);
        credentials.storeUsername(account.name);

        credentials.storeUrl(AccountsHelper.getUrl(this, account));

        String url = AccountsHelper.getUrl(this, account);

        credentials.storeUrl(url);

        if (changingUser) {
            lastUsedFragment = null;
            clearFragments();
        }
    }

    private void clearFragments() {
        reposFragment = null;
        eventsFragment = null;

        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (notificationsSizeCount > 0) {
            BadgeStyle badgeStyle =
                new BadgeStyle(BadgeStyle.Style.DEFAULT, R.layout.menu_badge, getResources().getColor(R.color.accent),
                    R.color.accent_dark, Color.WHITE, getResources().getDimensionPixelOffset(R.dimen.gapMicro));

            ActionItemBadge.update(this, menu.findItem(R.id.action_notifications), Octicons.Icon.oct_bell, badgeStyle,
                notificationsSizeCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.action_notifications));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotifications();
    }

    private void checkNotifications() {
        GetNotificationsClient client = new GetNotificationsClient(this);
        client.setOnResultCallback(new BaseClient.OnResultCallback<List<Notification>>() {
            @Override
            public void onResponseOk(List<Notification> notifications, Response r) {
                if (notifications != null) {
                    notificationsSizeCount = notifications.size();
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onFail(RetrofitError error) {

            }
        });
        client.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            Intent intent = SearchActivity.launchIntent(this);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_notifications) {
            openNotifications();
        }

        return false;
    }

    private void openNotifications() {
        Intent intent = NotificationsActivity.launchIntent(this);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment) {
        setFragment(fragment, true);
    }

    private void setFragment(Fragment fragment, boolean addToBackStack) {
        try {
            if (fragment != null && getSupportFragmentManager() != null) {
                this.lastUsedFragment = fragment;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (ft != null) {
                    ft.replace(R.id.content, fragment);
                    if (addToBackStack) {
                        ft.addToBackStack(null);
                    }
                    ft.commit();
                }
            }
        } catch (Exception e) {
            ErrorHandler.onError(this, "MainActivity.setFragment()", e);
        }
    }

    @Override
    public boolean onProfileSelected() {
        Intent launcherIntent = ProfileActivity.createLauncherIntent(this, selectedAccount);
        startActivityForResult(launcherIntent, PROFILE_REQUEST_CODE);
        return false;
    }

    @Override
    public boolean onReposSelected() {
        clearFragments();

        if (reposFragment == null) {
            reposFragment = GeneralReposFragment.newInstance();
        }

        setFragment(reposFragment, false);
        return true;
    }

    @Override
    public boolean onPeopleSelected() {
        setFragment(GeneralPeopleFragment.newInstance(), false);
        return false;
    }

    public boolean onGistsSelected() {
        Intent intent = GistsMainActivity.createLauncherIntent(this);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onUserEventsSelected() {
        String user = new StoreCredentials(this).getUserName();
        if (user != null) {
            if (eventsFragment == null) {
                eventsFragment = EventsListFragment.newInstance(user);
            }
        }
        setFragment(eventsFragment);
        return true;
    }

    @Override
    public boolean onSettingsSelected() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return false;
    }

    public void signOut() {
        if (selectedAccount != null) {
            removeAccount(selectedAccount, new BaseAccountsFragmentManager.RemoveAccountCallback() {
                @Override
                public void onAccountRemoved() {
                    StoreCredentials storeCredentials = new StoreCredentials(MainActivity.this);
                    storeCredentials.clear();

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onAboutSelected() {
        new LibsBuilder()
            //Pass the fields of your application to the lib so it can find all external lib information
            .withFields(R.string.class.getFields())
            .withActivityTitle(getString(R.string.app_name))
            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
            .withActivityTheme(R.style.AppTheme)
            .start(this);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (resultDrawer != null && resultDrawer.isDrawerOpen()) {
            resultDrawer.closeDrawer();
        } else {
            if (lastUsedFragment instanceof EventsListFragment) {
                finish();
            } else if (resultDrawer != null && (lastUsedFragment instanceof GeneralReposFragment
                || lastUsedFragment instanceof GeneralPeopleFragment)) {
                resultDrawer.setSelection(R.id.nav_drawer_events);
                clearFragments();
                onUserEventsSelected();
            }
        }
    }

    @Override
    public void onStop() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
