package com.alorma.github.ui.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.StyleRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alorma.data.oauth.GitskariosOauthRequestTokenClient;
import com.alorma.data.user.GitskariosUserClient;
import com.alorma.github.BuildConfig;
import com.alorma.github.Interceptor;
import com.alorma.github.R;
import com.alorma.github.ui.ErrorHandler;
import com.alorma.gitskarios.core.Token;
import com.alorma.data.oauth.AccountsHelper;
import com.alorma.github.sdk.security.GitHub;
import com.alorma.gitlabsdk.security.Gitlab;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosDeveloperCredentials;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;
import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AccountAuthenticatorActivity implements BaseDataSource.Callback<GitskariosUser> {

    public static final String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static final String ADDING_FROM_ACCOUNTS = "ADDING_FROM_ACCOUNTS";
    public static final String ADDING_FROM_APP = "ADDING_FROM_APP";
    public static final String FROM_DELETE = "FROM_DELETE";
    private static final String SKU_MULTI_ACCOUNT = "com.alorma.github.multiaccount";

    private SpotsDialog progressDialog;
    private String accessToken;
    private String scope;

    private IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    private Account[] accounts;
    private String purchaseId;
    private boolean fromApp;
    private boolean fromDeleteRepo;
    private ApiConnection apiConnection;

    /**
     * There is three ways to get to this activity:
     * 1. From Android launcher.
     * 2. After user's login.
     * 3. From application, it means, user wants to switch accounts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createBillingService();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final View loginButtonGithub = findViewById(R.id.login_github);
        loginButtonGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(new GitHub());
            }
        });

        final View gitlab_conf = findViewById(R.id.gitlab_conf);
        gitlab_conf.setEnabled(false);

        final View loginButtonGitab = findViewById(R.id.login_gitlab);
        final EditText gitlab_app_key = (EditText) findViewById(R.id.gitlab_app_key);
        final View gitlab_app_key_button = findViewById(R.id.gitlab_app_key_button);
        gitlab_app_key_button.setEnabled(false);
        gitlab_app_key.setEnabled(false);
        gitlab_app_key_button.setEnabled(false);
        loginButtonGitab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gitlab_conf.setEnabled(true);
                gitlab_app_key.setEnabled(true);
                gitlab_app_key_button.setEnabled(true);
            }
        });

        gitlab_app_key_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endAccess(gitlab_app_key.getText().toString(), null, new Gitlab());
            }
        });

        enableCreateGist(false);

        AccountManager accountManager = AccountManager.get(this);

        accounts = accountManager.getAccountsByType(getString(R.string.account_type));

        final boolean fromLogin = getIntent().getData() != null &&
                getIntent().getData().getScheme().equals(getString(R.string.oauth_scheme));
        final boolean fromAccounts = getIntent().getBooleanExtra(ADDING_FROM_ACCOUNTS, false);
        fromApp = getIntent().getBooleanExtra(ADDING_FROM_APP, false);
        fromDeleteRepo = getIntent().getBooleanExtra(FROM_DELETE, false);

        toolbar.setNavigationIcon(R.drawable.ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeLoginActivity(fromApp, fromDeleteRepo);
            }
        });

        if (accounts != null) {
            for (Account account : accounts) {
                checkAndEnableSyncAdapter(account);
            }
        }

        if (fromDeleteRepo) {
            openExternalLogin(new GitHub());
        } else if (fromLogin) {
            loginButtonGithub.setEnabled(false);
            showProgressDialog(R.style.SpotDialog_Login);
            Uri uri = getIntent().getData();
            String code = uri.getQueryParameter("code");

            fromDeleteRepo = Boolean.valueOf(uri.getQueryParameter("fromDeleteRepo"));

            handleLoginFromGithub(code, new GitHub());
        } else if (fromAccounts) {
            login(new GitHub());
        } else if (!fromApp && accounts != null && accounts.length > 0) {
            openMain();
        }
    }

    private void handleLoginFromGithub(String code, ApiConnection apiConnection) {
        BaseDataSource<Token> tokenTokenBaseDataSource =
                new GitskariosOauthRequestTokenClient(LoginActivity.this, code, apiConnection)
                        .create();
        if (tokenTokenBaseDataSource != null) {
            final ApiConnection finalConnection = apiConnection;
            tokenTokenBaseDataSource.executeAsync(new BaseDataSource.Callback<Token>() {
                @Override
                public void onResponse(Token token, Response response) {
                    if (token.access_token != null) {
                        endAccess(token.access_token, token.scope, finalConnection);
                    } else if (token.error != null) {
                        Toast.makeText(LoginActivity.this, token.error, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFail(RetrofitError error) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    ErrorHandler.onError(LoginActivity.this, "Login", error);
                }
            });
        }
    }

    private void closeLoginActivity(boolean fromApp, boolean fromDelete) {
        if (fromDelete) {
            setResult(RESULT_OK);
            finish();
        } else if (fromApp) {
            openMain();
        } else {
            finish();
        }
    }

    private void createBillingService() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    public void showProgressDialog(@StyleRes int style) {
        try {
            progressDialog = new SpotsDialog(this, style);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(ApiConnection apiConnection) {
        if (multipleAccountFeatureRequired()) {
            SKUTask task = new SKUTask();
            task.execute(SKU_MULTI_ACCOUNT);
        } else {
            openExternalLogin(apiConnection);
        }
    }

    private boolean multipleAccountFeatureRequired() {
        return !BuildConfig.DEBUG && accounts != null && accounts.length > 0;
    }

    private void openExternalLogin(ApiConnection client) {
        if (GitskariosDeveloperCredentials.getInstance().getProvider(client).getApiClient() == null) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title("API keys fail");
            builder.content("Credentials are not provided via GitskariosDeveloperCredentials.init().");
            builder.positiveText("OK");
            builder.show();
            return;
        }

        Uri callbackUri = Uri.EMPTY.buildUpon()
                .scheme(getString(R.string.oauth_scheme))
                .authority("oauth")
                .build();

        Uri uri = client.buildUri(callbackUri);

        final List<ResolveInfo> browserList = getBrowserList();

        final List<LabeledIntent> intentList = new ArrayList<>();

        for (final ResolveInfo resolveInfo : browserList) {
            final Intent newIntent = new Intent(Intent.ACTION_VIEW, uri);
            newIntent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.name));

            intentList.add(new LabeledIntent(newIntent,
                    resolveInfo.resolvePackageName,
                    resolveInfo.labelRes,
                    resolveInfo.icon));
        }

        final Intent chooser = Intent.createChooser(intentList.remove(0), "Choose your favorite browser");
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

        startActivity(chooser);
        finish();
    }

    private List<ResolveInfo> getBrowserList() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sometesturl.com"));

        return getPackageManager().queryIntentActivities(intent, 0);
    }

    private class SKUTask extends AsyncTask<String, Void, Bundle> {

        @Override
        protected Bundle doInBackground(String... strings) {
            ArrayList<String> skuList = new ArrayList<>();
            Collections.addAll(skuList, strings);
            Bundle querySkus = new Bundle();
            querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

            try {
                return mService.getPurchases(3, getPackageName(), "inapp", null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bundle ownedItems) {
            super.onPostExecute(ownedItems);
            if (ownedItems != null) {
                int response = ownedItems.getInt("RESPONSE_CODE");
                if (response == 0) {
                    ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");

                    if (ownedSkus != null) {
                        if (ownedSkus.size() == 0) {
                            showDialogBuyMultiAccount();
                        } else {
                            openExternalLogin(new GitHub());
                        }
                    }
                }
            }
        }

    }

    private void showDialogBuyMultiAccount() {
        try {
            purchaseId = UUID.randomUUID().toString();
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                    SKU_MULTI_ACCOUNT, "inapp", purchaseId);
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), 0, 0, 0);
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    String developerPayload = jo.getString("developerPayload");
                    if (developerPayload.equals(purchaseId) && SKU_MULTI_ACCOUNT.equals(sku)) {
                        openExternalLogin(new GitHub());
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    private void enableCreateGist(boolean b) {
        int flag = b ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        ComponentName componentName = new ComponentName(this, Interceptor.class);
        getPackageManager().setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP);
    }

    private void openMain() {
        enableCreateGist(true);
        MainActivity.startActivity(LoginActivity.this);
        finish();
    }

    private void endAccess(String accessToken, String scope, ApiConnection apiConnection) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.apiConnection = apiConnection;

        if (progressDialog != null) {
            progressDialog.setMessage(getString(R.string.loading_user));
        } else {
            showProgressDialog(R.style.SpotDialog_LoadingUser);
        }

        new GitskariosUserClient(this, accessToken).setApiConnection(apiConnection).create().executeAsync(this);

    }

    @Override
    public void onResponse(GitskariosUser user, Response r) {
        AccountManager accountManager = AccountManager.get(this);

        accounts = accountManager.getAccountsByType(getString(R.string.account_type));

        for (Account account : accounts) {
            if (account.name.equals(user.login)
                    && AccountsHelper.getApiConnectionType(this, account).equals(apiConnection.getType())) {
                removeAndAdd(account, user);
                return;
            }
        }

        addAccount(user);

    }

    private void removeAndAdd(Account account, final GitskariosUser user) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                        if (accountManagerFuture.isDone()) {
                            addAccount(user);
                        }
                    }
                };
                AccountManager.get(this).removeAccount(account, this, callback, new Handler());
            } else {
                AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>() {
                    @Override
                    public void run(AccountManagerFuture<Boolean> accountManagerFuture) {
                        if (accountManagerFuture.isDone()) {
                            addAccount(user);
                        }
                    }
                };
                AccountManager.get(this).removeAccount(account, callback, new Handler());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAccount(GitskariosUser user) {
        Account account = new Account(user.login, getString(R.string.account_type));
        Bundle userData = AccountsHelper.buildBundle(user.name, user.email, user.avatar_url, scope, apiConnection);
        userData.putString(AccountManager.KEY_AUTHTOKEN, accessToken);

        AccountManager accountManager = AccountManager.get(this);
        accountManager.addAccountExplicitly(account, null, userData);
        accountManager.setAuthToken(account, getString(R.string.account_type), accessToken);

        Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);

        setAccountAuthenticatorResult(result);

        checkAndEnableSyncAdapter(account);

        if (fromDeleteRepo) {
            setResult(RESULT_OK);
            finish();
        } else {
            openMain();
        }
    }

    private void checkAndEnableSyncAdapter(Account account) {
        ContentResolver.setIsSyncable(account, getString(R.string.account_type), ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE);
        if (ContentResolver.getSyncAutomatically(account, getString(R.string.account_type))) {
            ContentResolver.addPeriodicSync(
                    account,
                    getString(R.string.account_type),
                    Bundle.EMPTY,
                    1800);
            ContentResolver.setSyncAutomatically(account, getString(R.string.account_type), true);
        }
    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        closeLoginActivity(fromApp, fromDeleteRepo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
}
