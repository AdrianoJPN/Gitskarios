package com.alorma.githubintegration.user;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.user.RequestUserClient;
import com.alorma.githubintegration.mapper.user.UserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;
import com.alorma.gitskarios.core.client.BaseClient;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class GithubUserDataSource extends BaseDataSource<GitskariosUser> {
    private final Context context;
    private final String login;

    public GithubUserDataSource(Context context, String login) {
        super();
        this.context = context;
        this.login = login;
    }

    @Override
    public void executeAsync(Callback<GitskariosUser> callback) {
        RequestUserClient requestUserClient = new RequestUserClient(context, login);
        requestUserClient.setOnResultCallback(new BaseMapperCallback<User, GitskariosUser>(callback) {
            @Override
            protected BaseMapper<User, GitskariosUser> getMapper() {
                return new UserMapper();
            }
        });
        requestUserClient.execute();
    }
}
