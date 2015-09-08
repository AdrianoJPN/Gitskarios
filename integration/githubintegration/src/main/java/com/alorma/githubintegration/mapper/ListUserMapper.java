package com.alorma.githubintegration.mapper;

import android.support.annotation.NonNull;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class ListUserMapper extends BaseMapper<List<User>, List<GitskariosUser>>{

    private final UserMapper userMapper;

    public ListUserMapper() {
        userMapper = new UserMapper();
    }

    @NonNull
    @Override
    public List<GitskariosUser> toCore(@NonNull List<User> users) {
        List<GitskariosUser> userList = new ArrayList<>(users.size());

        for (User user : users) {
            userList.add(userMapper.toCore(user));
        }

        return userList;
    }
}
