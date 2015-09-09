package com.alorma.data.user;

import android.content.Context;

import com.alorma.github.sdk.security.GitHub;
import com.alorma.githubintegration.user.GithubAuthUserDataSource;
import com.alorma.githubintegration.user.GithubUserDataSource;
import com.alorma.gitlabintegration.user.GitlabAuthUserDataSource;
import com.alorma.gitlabintegration.user.GitlabUserDataSource;
import com.alorma.gitlabsdk.security.Gitlab;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

public class GitskariosUserClient implements GitskariosFactory<GitskariosUser> {

    private Context context;
    private GitskariosUser user;
    private ApiConnection apiConnection;
    private String access_token;

    public GitskariosUserClient(Context context) {
        this.context = context;
    }

    public GitskariosUserClient(Context context, GitskariosUser user) {
        this.context = context;
        this.user = user;
    }

    public GitskariosUserClient(Context context, String access_token) {
        this.context = context;
        this.access_token = access_token;
    }

    @Override
    public BaseDataSource<GitskariosUser> create() {
        if (apiConnection != null) {
            if (apiConnection.getType().equals(new GitHub().getType())) {
                if (user != null && user.login != null) {
                    return new GithubUserDataSource(context, user.login);
                } else {
                    return new GithubAuthUserDataSource(context, access_token);
                }
            } else if (apiConnection.getType().equals(new Gitlab().getType())) {
                if (user != null && user.id != null) {
                    return new GitlabUserDataSource(context, user.id);
                } else {
                    return new GitlabAuthUserDataSource(context, access_token);
                }
            }
        }
        return null;
    }

    @Override
    public GitskariosFactory<GitskariosUser> setApiConnection(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
        return this;
    }

}
