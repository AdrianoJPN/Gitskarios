package com.alorma.github.ui.callbacks;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.githubintegration.mapper.user.UserMapper;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;
import com.alorma.gitskarios.core.client.BaseClient;

/**
 * Created by a557114 on 08/09/2015.
 */
@Deprecated
public class UserCallback extends TransitionalCallback<User, GitskariosUser>{
    public UserCallback(BaseClient.OnResultCallback<GitskariosUser> callback) {
        super(callback);
    }

    @Override
    protected BaseMapper<User, GitskariosUser> geMapper() {
        return new UserMapper();
    }
}
