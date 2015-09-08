package com.alorma.githubintegration.user;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.user.RequestUserClient;
import com.alorma.githubintegration.mapper.UserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubUserDataSource extends BaseDataSource<GithubClient<User>, User, GitskariosUser> {
    private final Context context;
    private final String login;

    public GithubUserDataSource(Context context, String login) {
        super();
        this.context = context;
        this.login = login;
    }

    @Override
    public GithubClient<User> getApiClient() {
        return new RequestUserClient(context, login);
    }

    @Override
    public BaseMapper<User, GitskariosUser> getMapper() {
        return new UserMapper();
    }
}
