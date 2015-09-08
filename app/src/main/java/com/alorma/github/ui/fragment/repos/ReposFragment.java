package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;

import com.alorma.data.repos.list.GitsakriosUserRepositoriesClient;
import com.alorma.github.R;

public class ReposFragment extends BaseReposListFragment {

    private String username;

    public static ReposFragment newInstance() {
        return new ReposFragment();
    }

    public static ReposFragment newInstance(String username) {
        ReposFragment reposFragment = new ReposFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            reposFragment.setArguments(bundle);
        }
        return reposFragment;
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    protected void executeRequest() {
        super.executeRequest();

        new GitsakriosUserRepositoriesClient(getActivity(), username).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        new GitsakriosUserRepositoriesClient(getActivity(), username, page).create().executeAsync(this);
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_repositories;
    }

}
