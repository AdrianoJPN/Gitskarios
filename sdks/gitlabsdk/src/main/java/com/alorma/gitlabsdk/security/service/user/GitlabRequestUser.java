package com.alorma.gitlabsdk.security.service.user;

import android.content.Context;

import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;
import com.alorma.gitlabsdk.security.client.GitlabClient;

import retrofit.RestAdapter;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitlabRequestUser extends GitlabClient<GitlabUser> {
    private int username;

    public GitlabRequestUser(Context context, int username) {
        super(context);
        this.username = username;
    }

    @Override
    protected void executeService(RestAdapter restAdapter) {
        restAdapter.create(UserService.class).getSingleUser(String.valueOf(username), this);
    }

    @Override
    protected GitlabUser executeServiceSync(RestAdapter restAdapter) {
        return restAdapter.create(UserService.class).getSingleUser(String.valueOf(username));
    }
}
