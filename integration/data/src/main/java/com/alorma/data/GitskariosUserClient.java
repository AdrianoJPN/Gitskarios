package com.alorma.data;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.githubintegration.mapper.GithubAuthUserDataSource;
import com.alorma.githubintegration.mapper.GithubUserDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosUserClient implements GitskariosFactory<GithubClient<User>, User, GitskariosUser> {

    private Context context;
    private String login;

    public GitskariosUserClient(Context context) {
        this.context = context;
    }
    public GitskariosUserClient(Context context, String login) {
        this.context = context;
        this.login = login;
    }

    @Override
    public BaseDataSource<GithubClient<User>, User, GitskariosUser> create() {
        if (login != null) {
            return new GithubUserDataSource(context, login);
        } else {
            return new GithubAuthUserDataSource(context);
        }
    }
}
