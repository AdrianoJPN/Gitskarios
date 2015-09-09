package com.alorma.gitlabsdk.security.service.user;

import android.content.Context;

import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;
import com.alorma.gitlabsdk.security.client.GitlabClient;

import retrofit.RestAdapter;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitlabRequestAuthUser extends GitlabClient<GitlabUser> {

    private String access_token;

    public GitlabRequestAuthUser(Context context, String access_token) {
        super(context);
        this.access_token = access_token;
    }

    @Override
    protected void executeService(RestAdapter restAdapter) {
        restAdapter.create(UserService.class).me(this);
    }

    @Override
    protected GitlabUser executeServiceSync(RestAdapter restAdapter) {
        return restAdapter.create(UserService.class).me();
    }

    @Override
    protected String getToken() {
        if (access_token != null) {
            return access_token;
        } else {
            return super.getToken();
        }
    }
}
