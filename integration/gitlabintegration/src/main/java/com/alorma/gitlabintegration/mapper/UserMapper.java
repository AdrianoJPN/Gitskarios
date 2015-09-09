package com.alorma.gitlabintegration.mapper;

import android.support.annotation.NonNull;

import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

/**
 * Created by a557114 on 09/09/2015.
 */
public class UserMapper implements BaseMapper<GitlabUser, GitskariosUser> {
    @NonNull
    @Override
    public GitskariosUser toCore(@NonNull GitlabUser gitlabUser) {
        GitskariosUser user = new GitskariosUser();
        user.id = gitlabUser.id;
        user.login = gitlabUser.username;
        user.name = gitlabUser.name;
        user.avatar_url = gitlabUser.avatar_url;
        return user;
    }
}
