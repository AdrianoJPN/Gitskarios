package com.alorma.githubintegration.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.user.UserFollowingClient;
import com.alorma.githubintegration.mapper.user.list.ListUserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class GithubFollowingUsersDataSource
        extends BaseDataSource<List<GitskariosUser>>
        implements Paginated {

    private Context context;
    private String login;
    private int page;

    public GithubFollowingUsersDataSource(Context context, String login) {
        this.context = context;
        this.login = login;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosUser>> callback) {
        UserFollowingClient client;
        if (page == 0) {
            client = new UserFollowingClient(context, login);
        } else {
            client = new UserFollowingClient(context, login, page);
        }

        client.setOnResultCallback(new BaseMapperCallback<List<User>, List<GitskariosUser>>(callback) {
            @Override
            protected BaseMapper<List<User>, List<GitskariosUser>> getMapper() {
                return new ListUserMapper();
            }
        });
        client.execute();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
