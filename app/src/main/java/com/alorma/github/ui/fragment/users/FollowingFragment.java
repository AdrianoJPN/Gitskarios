package com.alorma.github.ui.fragment.users;

import android.os.Bundle;

import com.alorma.github.R;
import com.alorma.github.sdk.services.user.UserFollowingClient;
import com.alorma.github.ui.callbacks.ListUserCallback;

/**
 * Created by Bernat on 13/07/2014.
 */
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
        UserFollowingClient client = new UserFollowingClient(getActivity(), username);
        client.setOnResultCallback(new ListUserCallback(this));
        client.execute();
    }

    @Override
    protected void executePaginatedRequest(int page) {
        UserFollowingClient client = new UserFollowingClient(getActivity(), username, page);
        client.setOnResultCallback(new ListUserCallback(this));
        client.execute();
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
