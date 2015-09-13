package com.alorma.github.ui.presenter;

import android.support.v4.app.Fragment;

import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.ui.fragment.commit.CommitsListFragment;
import com.alorma.github.ui.fragment.detail.repo.PermissionsManager;
import com.alorma.github.ui.fragment.detail.repo.RepoAboutFragment;
import com.alorma.github.ui.fragment.detail.repo.RepoContributorsFragment;
import com.alorma.github.ui.fragment.detail.repo.SourceListFragment;
import com.alorma.github.ui.fragment.issues.IssuesListFragment;
import com.alorma.github.ui.fragment.issues.PullRequestsListFragment;
import com.alorma.github.ui.fragment.releases.RepoReleasesFragment;
import com.alorma.gitskarios.core.bean.dto.GitskariosPermissions;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositoryDetailPresenter {

    private RepoDetailPresenterCallback presenterCallback;

    public void load(RepoInfo repoInfo) {
       /*GetRepoClient repoClient = new GetRepoClient(this, repoInfo);
        repoClient.setOnResultCallback(this);
        repoClient.execute();*/

        if (presenterCallback != null) {
            GitskariosRepository repository = new GitskariosRepository();
            repository.name = repoInfo.name;
            repository.owner = new GitskariosUser();
            repository.owner.login = repoInfo.owner;
            repository.owner.avatar_url = "https://secure.gravatar.com/avatar/ad2976458a5f7970664a6e28a558c002?s=40\\u0026d=identicon";
            repository.stargazers_count = 20;
            repository.subscribers_count = 20;
            repository.forks_count = 20;
            repository.created_at = new Date();

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

    private List<Fragment> createListFragments(GitskariosRepository currentRepo) {
        List<Fragment> listFragments = new ArrayList<>();
        if (currentRepo != null) {
            RepoAboutFragment aboutFragment = RepoAboutFragment.newInstance(currentRepo, getRepoInfo(currentRepo));
            SourceListFragment sourceListFragment = SourceListFragment.newInstance(getRepoInfo(currentRepo));
            CommitsListFragment commitsListFragment = CommitsListFragment.newInstance(getRepoInfo(currentRepo));
            IssuesListFragment issuesListFragment = IssuesListFragment.newInstance(getRepoInfo(currentRepo), false);
            PullRequestsListFragment pullRequestsListFragment = PullRequestsListFragment.newInstance(getRepoInfo(currentRepo));
            RepoReleasesFragment repoReleasesFragment = RepoReleasesFragment.newInstance(getRepoInfo(currentRepo));
            RepoContributorsFragment repoCollaboratorsFragment = RepoContributorsFragment.newInstance(getRepoInfo(currentRepo), currentRepo.owner);

            listFragments.add(aboutFragment);
//            listFragments.add(sourceListFragment);
//            listFragments.add(commitsListFragment);
//            listFragments.add(issuesListFragment);
//            listFragments.add(pullRequestsListFragment);
//            listFragments.add(repoReleasesFragment);
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

    public interface RepoDetailPresenterCallback {
        void setUpWithRepo(GitskariosRepository repo, List<Fragment> fragments);
    }
}
