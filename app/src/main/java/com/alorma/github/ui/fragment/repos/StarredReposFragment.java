package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;

import com.alorma.data.repos.list.GitsakriosStarredRepositoriesClient;
import com.alorma.github.R;
import com.alorma.github.sdk.services.repos.GithubReposClient;
import com.alorma.github.sdk.services.repos.StarredReposClient;
import com.alorma.github.ui.callbacks.ListReposCallback;

public class StarredReposFragment extends BaseReposListFragment {

    private String username;

    public static StarredReposFragment newInstance() {
        return new StarredReposFragment();
    }

    public static StarredReposFragment newInstance(String username) {
        StarredReposFragment reposFragment = new StarredReposFragment();
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
        new GitsakriosStarredRepositoriesClient(getActivity(), username).create().executeAsync(this);
    }


    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        new GitsakriosStarredRepositoriesClient(getActivity(), username, page).create().executeAsync(this);
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_starred_repositories;
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }
}
