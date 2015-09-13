package com.alorma.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.request.RepoRequestDTO;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.repo.GetRepoBranchesClient;
import com.alorma.github.ui.activity.base.BackActivity;
import com.alorma.github.ui.callbacks.DialogBranchesCallback;
import com.alorma.github.ui.fragment.detail.repo.BackManager;
import com.alorma.github.ui.fragment.detail.repo.BranchManager;
import com.alorma.github.ui.listeners.TitleProvider;
import com.alorma.github.ui.presenter.RepositoryDetailPresenter;
import com.alorma.github.utils.ShortcutUtils;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.List;

/**
 * Created by Bernat on 17/07/2014.
 */
public class RepoDetailActivity extends BackActivity
        implements AdapterView.OnItemSelectedListener,
        RepositoryDetailPresenter.RepoDetailPresenterCallback {

    public static final String REPO_INFO = "REPO_INFO";
    public static final String REPO_INFO_NAME = "REPO_INFO_NAME";
    public static final String REPO_INFO_OWNER = "REPO_INFO_OWNER";

    private static final int EDIT_REPO = 464;

    private GitskariosRepository currentRepo;
    private ViewPager viewPager;
    private List<Fragment> listFragments;
    private TabLayout tabLayout;
    private RepoInfo requestRepoInfo;
    private RepositoryDetailPresenter repositoryDetailPresenter;

    public static Intent createLauncherIntent(Context context, RepoInfo repoInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(REPO_INFO, repoInfo);

        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createShortcutLauncherIntent(Context context, RepoInfo repoInfo) {
        Bundle bundle = new Bundle();
        bundle.putString(REPO_INFO_NAME, repoInfo.name);
        bundle.putString(REPO_INFO_OWNER, repoInfo.owner);

        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        if (getIntent().getExtras() != null) {
            RepoInfo repoInfo = getIntent().getExtras().getParcelable(REPO_INFO);

            if (repoInfo == null) {
                if (getIntent().getExtras().containsKey(REPO_INFO_NAME) && getIntent().getExtras().containsKey(REPO_INFO_OWNER)) {
                    String name = getIntent().getExtras().getString(REPO_INFO_NAME);
                    String owner = getIntent().getExtras().getString(REPO_INFO_OWNER);

                    repoInfo = new RepoInfo();
                    repoInfo.name = name;
                    repoInfo.owner = owner;
                }
            }

            if (repoInfo != null) {
                setTitle(repoInfo.name);

                tabLayout = (TabLayout) findViewById(R.id.tabStrip);

                viewPager = (ViewPager) findViewById(R.id.pager);

                load(repoInfo);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void load(RepoInfo repoInfo) {
        this.requestRepoInfo = repoInfo;

        repositoryDetailPresenter = new RepositoryDetailPresenter();
        repositoryDetailPresenter.setPresenterCallback(this).load(this, repoInfo);
    }


    private class NavigationPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> listFragments;

        public NavigationPagerAdapter(FragmentManager fm, List<Fragment> listFragments) {
            super(fm);
            this.listFragments = listFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (listFragments.get(position) != null && listFragments.get(position) instanceof TitleProvider) {
                return getString(((TitleProvider) listFragments.get(position)).getTitle());
            }
            return "";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.repo_detail_activity, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (menu.findItem(R.id.action_manage_repo) == null) {
            if (currentRepo != null && currentRepo.gitskariosPermissions != null) {
                if (currentRepo.gitskariosPermissions.admin) {
                    getMenuInflater().inflate(R.menu.repo_detail_activity_permissions, menu);
                }
            }
        }

        MenuItem item = menu.findItem(R.id.share_repo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_menu_share_mtrl_alpha, getTheme()));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_menu_share_mtrl_alpha));
        }

        MenuItem menuChangeBranch = menu.findItem(R.id.action_repo_change_branch);

        Drawable changeBranch = new IconicsDrawable(this, Octicons.Icon.oct_git_branch).actionBar().colorRes(R.color.white);

        if (menuChangeBranch != null) {
            menuChangeBranch.setIcon(changeBranch);
        }

        return true;
    }

    private Intent getShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Intent.EXTRA_SUBJECT, currentRepo.full_name);
        intent.putExtra(Intent.EXTRA_TEXT, currentRepo.external_url);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.share_repo) {
            if (currentRepo != null) {
                Intent intent = getShareIntent();
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.action_repo_change_branch) {
            changeBranch();
        } else if (item.getItemId() == R.id.action_manage_repo) {
            Intent intent = ManageRepositoryActivity.createIntent(this, repositoryDetailPresenter.getRepoInfo(currentRepo), createRepoRequest());
            startActivityForResult(intent, EDIT_REPO);
        } else if (item.getItemId() == R.id.action_add_shortcut) {
            ShortcutUtils.addShortcut(this, repositoryDetailPresenter.getRepoInfo(currentRepo));
        }

        return false;
    }

    private RepoRequestDTO createRepoRequest() {
        RepoRequestDTO dto = new RepoRequestDTO();

        dto.name = currentRepo.name;
        dto.description = currentRepo.description;
        dto.default_branch = currentRepo.default_branch;
        dto.has_downloads = currentRepo.has_downloads;
        dto.has_wiki = currentRepo.has_wiki;
        dto.has_issues = currentRepo.has_issues;
        dto.homepage = currentRepo.homepage;

        return dto;
    }

    private void changeBranch() {
        GetRepoBranchesClient repoBranchesClient = new GetRepoBranchesClient(this, repositoryDetailPresenter.getRepoInfo(currentRepo));
        repoBranchesClient.setOnResultCallback(new DialogBranchesCallback(this, repositoryDetailPresenter.getRepoInfo(currentRepo)) {
            @Override
            protected void onBranchSelected(String branch) {
                currentRepo.default_branch = branch;
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(branch);
                }
                if (listFragments != null) {
                    for (Fragment fragment : listFragments) {
                        if (fragment instanceof BranchManager) {
                            ((BranchManager) fragment).setCurrentBranch(branch);
                        }
                    }
                }
            }

            @Override
            protected void onNoBranches() {

            }
        });
        repoBranchesClient.execute();
    }


    @Override
    public void setUpWithRepo(GitskariosRepository repo, List<Fragment> fragments) {
        hideProgressDialog();
        if (repo != null) {
            this.currentRepo = repo;
            invalidateOptionsMenu();

            setTitle(currentRepo.name);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(repositoryDetailPresenter.getRepoInfo(currentRepo).branch);
            }

            viewPager.setAdapter(new NavigationPagerAdapter(getSupportFragmentManager(), fragments));
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REPO) {
            if (resultCode == RESULT_OK && data != null) {
                RepoRequestDTO repoRequestDTO = data.getParcelableExtra(ManageRepositoryActivity.CONTENT);
                showProgressDialog(R.style.SpotDialog_edit_repo);
                // TODO
                /*EditRepoClient editRepositoryClient = new EditRepoClient(this, getRepoInfo(), repoRequestDTO);
                editRepositoryClient.setOnResultCallback(this);
                editRepositoryClient.execute();*/
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        int currentItem = viewPager.getCurrentItem();

        if (listFragments != null && currentItem >= 0 && currentItem < listFragments.size()) {
            Fragment fragment = listFragments.get(currentItem);
            if (fragment != null && fragment instanceof BackManager) {
                if (((BackManager) fragment).onBackPressed()) {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
