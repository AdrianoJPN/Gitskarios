package com.alorma.github.ui.fragment.users;

import android.os.Bundle;

import com.alorma.data.user.list.GitskariosFollowingUsersClient;
import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class FollowingFragment extends BaseUsersListFragment {
    private String username;

    public static FollowingFragment newInstance() {
        return new FollowingFragment();
    }

    public static FollowingFragment newInstance(String username) {
        FollowingFragment followingFragment = new FollowingFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            followingFragment.setArguments(bundle);
        }
        return followingFragment;
    }

    @Override
    protected void executeRequest() {
        new GitskariosFollowingUsersClient(getActivity(), username).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        new GitskariosFollowingUsersClient(getActivity(), username, page).create().executeAsync(this);
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_followings;
    }

}
