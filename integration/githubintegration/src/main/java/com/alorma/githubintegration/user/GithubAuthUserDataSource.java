package com.alorma.githubintegration.user;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.githubintegration.mapper.user.UserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

public class GithubAuthUserDataSource extends BaseDataSource<GitskariosUser> {

    private final Context context;
    private String access_token;

    public GithubAuthUserDataSource(Context context, String access_token) {
        this.context = context;
        this.access_token = access_token;
    }

    @Override
    public void executeAsync(Callback<GitskariosUser> callback) {
        GetAuthUserClient getAuthUserClient = new GetAuthUserClient(context, access_token);
        getAuthUserClient.setOnResultCallback(new BaseMapperCallback<User, GitskariosUser>(callback) {
            @Override
            protected BaseMapper<User, GitskariosUser> getMapper() {
                return new UserMapper();
            }
        });
        getAuthUserClient.execute();
    }
}
