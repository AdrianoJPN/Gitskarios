package com.alorma.data.user;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.githubintegration.user.GithubAuthUserDataSource;
import com.alorma.githubintegration.user.GithubUserDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

public class GitskariosUserClient implements GitskariosFactory<User, GitskariosUser> {

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
    public BaseDataSource<User, GitskariosUser> create() {
        if (login != null) {
            return new GithubUserDataSource(context, login);
        } else {
            return new GithubAuthUserDataSource(context);
        }
    }
}
