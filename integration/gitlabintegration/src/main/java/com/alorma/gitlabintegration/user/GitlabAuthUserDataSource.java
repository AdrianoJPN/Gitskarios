package com.alorma.gitlabintegration.user;

import android.content.Context;

import com.alorma.gitlabintegration.mapper.UserMapper;
import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;
import com.alorma.gitlabsdk.security.service.user.GitlabRequestAuthUser;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitlabAuthUserDataSource extends BaseDataSource<GitskariosUser> {
    private final Context context;
    private String access_token;

    public GitlabAuthUserDataSource(Context context, String access_token) {
        this.context = context;
        this.access_token = access_token;
    }

    @Override
    public void executeAsync(Callback<GitskariosUser> callback) {
        GitlabRequestAuthUser requestUserClient = new GitlabRequestAuthUser(context, access_token);
        requestUserClient.setOnResultCallback(new BaseMapperCallback<GitlabUser, GitskariosUser>(callback) {
            @Override
            protected BaseMapper<GitlabUser, GitskariosUser> getMapper() {
                return new UserMapper();
            }
        });
        requestUserClient.execute();
    }
}
