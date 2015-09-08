package com.alorma.github.ui.callbacks;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.githubintegration.mapper.ListUserMapper;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;
import com.alorma.gitskarios.core.client.BaseClient;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
@Deprecated
public class ListUserCallback extends TransitionalCallback<List<User>, List<GitskariosUser>> {
    public ListUserCallback(BaseClient.OnResultCallback<List<GitskariosUser>> callback) {
        super(callback);
    }

    @Override
    protected BaseMapper<List<User>, List<GitskariosUser>> geMapper() {
        return new ListUserMapper();
    }
}
