package com.alorma.github.ui.fragment.users;

import android.os.Bundle;

import com.alorma.data.user.list.GitskariosFollowersUsersClient;
import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class FollowersFragment extends BaseUsersListFragment {
    private String username;

    public static FollowersFragment newInstance() {
        return new FollowersFragment();
    }

    public static FollowersFragment newInstance(String username) {
        FollowersFragment followersFragment = new FollowersFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            followersFragment.setArguments(bundle);
        }
        return followersFragment;
    }

    @Override
    protected void executeRequest() {
        super.executeRequest();
        BaseDataSource<List<User>, List<GitskariosUser>> dataSource = new GitskariosFollowersUsersClient(getActivity(), username).create();
        dataSource.executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        BaseDataSource<List<User>, List<GitskariosUser>> dataSource = new GitskariosFollowersUsersClient(getActivity(), username, page).create();
        dataSource.executeAsync(this);
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_followers;
    }
}

