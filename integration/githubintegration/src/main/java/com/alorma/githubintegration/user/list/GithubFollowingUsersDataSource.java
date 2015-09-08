package com.alorma.githubintegration.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.user.UserFollowingClient;
import com.alorma.githubintegration.mapper.user.list.ListUserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class GithubFollowingUsersDataSource
        extends BaseDataSource<List<User>, List<GitskariosUser>>
        implements Paginated {

    private Context context;
    private String login;
    private int page;

    public GithubFollowingUsersDataSource(Context context, String login) {
        this.context = context;
        this.login = login;
    }

    @Override
    public GithubClient<List<User>> getApiClient() {
        if (page == 0) {
            return new UserFollowingClient(context, login);
        } else {
            return new UserFollowingClient(context, login, page);
        }
    }

    @Override
    public BaseMapper<List<User>, List<GitskariosUser>> getMapper() {
        return new ListUserMapper();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
