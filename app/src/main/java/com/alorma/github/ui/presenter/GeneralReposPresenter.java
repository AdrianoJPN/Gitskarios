package com.alorma.github.ui.presenter;

import android.support.v4.app.Fragment;

import com.alorma.github.ui.fragment.repos.ContributedReposFragment;
import com.alorma.github.ui.fragment.repos.ReposFragment;
import com.alorma.github.ui.fragment.repos.ReposFragmentFromOrgs;
import com.alorma.github.ui.fragment.repos.StarredReposFragment;
import com.alorma.github.ui.fragment.repos.WatchedReposFragment;
import com.alorma.gitskarios.core.ApiConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a557114 on 13/09/2015.
 */
public class GeneralReposPresenter {

    private GeneralReposPresenterCallback generalReposPresenterCallback;
    private ApiConnection apiConnection;

    public void setApiConnection(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
        if (generalReposPresenterCallback != null) {
            generalReposPresenterCallback.createViewPager(getItems());
        }
    }

    private List<Fragment> getItems() {
        return (apiConnection != null && apiConnection.getType().equals("github")) ? getGithubItems() : getGitlabItems();
    }

    public void setGeneralReposPresenterCallback(GeneralReposPresenterCallback generalReposPresenterCallback) {
        this.generalReposPresenterCallback = generalReposPresenterCallback;
    }

    private List<Fragment> getGithubItems() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ReposFragment.newInstance());
        fragments.add(StarredReposFragment.newInstance());
        fragments.add(WatchedReposFragment.newInstance());
        fragments.add(ContributedReposFragment.newInstance());
        fragments.add(ReposFragmentFromOrgs.newInstance());
        return fragments;
    }

    private List<Fragment> getGitlabItems() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ReposFragment.newInstance());
        return fragments;
    }

    public interface GeneralReposPresenterCallback {
        void createViewPager(List<Fragment> items);
    }
}
