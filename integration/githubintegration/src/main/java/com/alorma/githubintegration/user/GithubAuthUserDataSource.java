package com.alorma.githubintegration.user;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.githubintegration.mapper.user.UserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubAuthUserDataSource extends BaseDataSource<GithubClient<User>, User, GitskariosUser> {
    private final Context context;

    public GithubAuthUserDataSource(Context context) {
        super();
        this.context = context;
    }

    @Override
    public GithubClient<User> getApiClient() {
        return new GetAuthUserClient(context);
    }

    @Override
    public BaseMapper<User, GitskariosUser> getMapper() {
        return new UserMapper();
    }
}
