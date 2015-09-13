package com.alorma.github.ui.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.alorma.data.GitskariosRepositoryClient;
import com.alorma.github.GitskariosApplication;
import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.ui.fragment.commit.CommitsListFragment;
import com.alorma.github.ui.fragment.detail.repo.PermissionsManager;
import com.alorma.github.ui.fragment.detail.repo.RepoAboutFragment;
import com.alorma.github.ui.fragment.detail.repo.RepoContributorsFragment;
import com.alorma.github.ui.fragment.detail.repo.SourceListFragment;
import com.alorma.github.ui.fragment.issues.IssuesListFragment;
import com.alorma.github.ui.fragment.issues.PullRequestsListFragment;
import com.alorma.github.ui.fragment.releases.RepoReleasesFragment;
import com.alorma.githubintegration.mapper.repo.RepositoryMapper;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.bean.dto.GitskariosPermissions;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.client.BaseClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RepositoryDetailPresenter implements BaseDataSource.Callback<GitskariosRepository> {

    private RepoDetailPresenterCallback presenterCallback;

    public void load(Context context, RepoInfo repoInfo) {
        GitskariosRepositoryClient client = new GitskariosRepositoryClient(context, repoInfo);
        ApiConnection apiConnection = ((GitskariosApplication) context.getApplicationContext()).getApiConnection();
        client.setApiConnection(apiConnection);
        BaseDataSource<GitskariosRepository> dataSource = client.create();
        if (dataSource != null) {
            dataSource.executeAsync(this);
        }
    }

    private List<Fragment> createListFragments(GitskariosRepository currentRepo) {
        List<Fragment> listFragments = new ArrayList<>();
        if (currentRepo != null) {
            RepoAboutFragment aboutFragment = RepoAboutFragment.newInstance(currentRepo, getRepoInfo(currentRepo));
            listFragments.add(aboutFragment);

            SourceListFragment sourceListFragment = SourceListFragment.newInstance(getRepoInfo(currentRepo));
//            listFragments.add(sourceListFragment);


            CommitsListFragment commitsListFragment = CommitsListFragment.newInstance(getRepoInfo(currentRepo));
//            listFragments.add(commitsListFragment);

            if (currentRepo.has_issues) {
                IssuesListFragment issuesListFragment = IssuesListFragment.newInstance(getRepoInfo(currentRepo), false);
                listFragments.add(issuesListFragment);
            }

            if (currentRepo.has_merge_request) {
                PullRequestsListFragment pullRequestsListFragment = PullRequestsListFragment.newInstance(getRepoInfo(currentRepo));
                listFragments.add(pullRequestsListFragment);
            }

            if (currentRepo.has_downloads) {
                RepoReleasesFragment repoReleasesFragment = RepoReleasesFragment.newInstance(getRepoInfo(currentRepo));
//            listFragments.add(repoReleasesFragment);
            }
            RepoContributorsFragment repoCollaboratorsFragment = RepoContributorsFragment.newInstance(getRepoInfo(currentRepo), currentRepo.owner);

//            listFragments.add(repoCollaboratorsFragment);
        }
        return listFragments;
    }

    public RepoInfo getRepoInfo(GitskariosRepository currentRepo) {
        RepoInfo repoInfo = new RepoInfo();
        if (currentRepo != null) {
            if (currentRepo.owner != null) {
                repoInfo.owner = currentRepo.owner.login;
            }
            repoInfo.name = currentRepo.name;
            repoInfo.branch = currentRepo.default_branch;
            /*if (requestRepoInfo != null && requestRepoInfo.branch != null) {
                repoInfo.branch = requestRepoInfo.branch;
            } else {
                repoInfo.branch = currentRepo.default_branch;
            }*/
        }

        return repoInfo;
    }

    public RepositoryDetailPresenter setPresenterCallback(RepoDetailPresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
        return this;
    }

    @Override
    public void onResponse(GitskariosRepository repository, Response r) {
        if (presenterCallback != null) {

            List<Fragment> listFragments = createListFragments(repository);

            if (listFragments != null) {
                for (Fragment fragment : listFragments) {
                    if (fragment instanceof PermissionsManager) {
                        GitskariosPermissions permissions = repository.gitskariosPermissions;
                        ((PermissionsManager) fragment).setPermissions(permissions.admin, permissions.push, permissions.pull);
                    }
                }
            }

            presenterCallback.setUpWithRepo(repository, listFragments);
        }
    }

    @Override
    public void onFail(RetrofitError error) {

    }

    public interface RepoDetailPresenterCallback {
        void setUpWithRepo(GitskariosRepository repo, List<Fragment> fragments);
    }
}
