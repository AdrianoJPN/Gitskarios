package com.alorma.gitlabintegration.user;

import android.content.Context;

import com.alorma.gitlabintegration.mapper.UserMapper;
import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;
import com.alorma.gitlabsdk.security.service.user.GitlabRequestUser;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitlabUserDataSource extends BaseDataSource<GitskariosUser> {
    private final Context context;
    private int id;

    public GitlabUserDataSource(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public void executeAsync(Callback<GitskariosUser> callback) {
        GitlabRequestUser requestUserClient = new GitlabRequestUser(context, id);
        requestUserClient.setOnResultCallback(new BaseMapperCallback<GitlabUser, GitskariosUser>(callback) {
            @Override
            protected BaseMapper<GitlabUser, GitskariosUser> getMapper() {
                return new UserMapper();
            }
        });
        requestUserClient.execute();
    }
}
