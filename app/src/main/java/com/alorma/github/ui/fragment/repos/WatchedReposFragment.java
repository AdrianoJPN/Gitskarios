package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;

import com.alorma.data.repos.list.GitskariosWatchedRepositoriesClient;
import com.alorma.github.R;
import com.alorma.github.sdk.services.repos.WatchedReposClient;
import com.alorma.github.ui.callbacks.ListReposCallback;

public class WatchedReposFragment extends BaseReposListFragment {

    private String username;

    public static WatchedReposFragment newInstance() {
        return new WatchedReposFragment();
    }

    public static WatchedReposFragment newInstance(String username) {
        WatchedReposFragment reposFragment = new WatchedReposFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            reposFragment.setArguments(bundle);
        }
        return reposFragment;
    }

    @Override
    protected void executeRequest() {
        super.executeRequest();
        new GitskariosWatchedRepositoriesClient(getActivity(), username).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        new GitskariosWatchedRepositoriesClient(getActivity(), username, page).create().executeAsync(this);
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_watched_repositories;
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }
}
