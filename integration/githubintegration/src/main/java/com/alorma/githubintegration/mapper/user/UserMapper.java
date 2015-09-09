package com.alorma.githubintegration.mapper.user;

import android.support.annotation.NonNull;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.bean.dto.response.UserType;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;
import com.alorma.gitskarios.core.bean.dto.GitskariosUserType;

/**
 * Created by a557114 on 08/09/2015.
 */
public class UserMapper implements BaseMapper<User, GitskariosUser> {

    @NonNull
    @Override
    public GitskariosUser toCore(@NonNull User user) {
        GitskariosUser gitskariosUser = new GitskariosUser();

        gitskariosUser.id = user.id;
        gitskariosUser.name = user.name;
        gitskariosUser.login = user.login;
        gitskariosUser.email = user.email;
        gitskariosUser.created_at = user.created_at;
        if (user.type == UserType.Organization) {
            gitskariosUser.type = GitskariosUserType.Organization;
        } else {
            gitskariosUser.type = GitskariosUserType.User;
        }
        gitskariosUser.avatar_url = user.avatar_url;
        gitskariosUser.company = user.company;
        gitskariosUser.location = user.location;
        gitskariosUser.public_gists = user.public_gists;
        gitskariosUser.public_repos = user.public_repos;

        return gitskariosUser;
    }
}
