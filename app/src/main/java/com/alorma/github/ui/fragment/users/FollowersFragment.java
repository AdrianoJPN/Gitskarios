package com.alorma.github.ui.fragment.users;

import android.os.Bundle;

import com.alorma.github.R;
import com.alorma.github.sdk.services.user.UserFollowersClient;
import com.alorma.github.ui.callbacks.ListUserCallback;

/**
 * Created by Bernat on 13/07/2014.
 */
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
        UserFollowersClient client = new UserFollowersClient(getActivity(), username);

        client.setOnResultCallback(new ListUserCallback(this));
        client.execute();
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        UserFollowersClient client = new UserFollowersClient(getActivity(), username, page);
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
        return R.string.no_followers;
    }
}

