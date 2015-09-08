package com.alorma.data.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.githubintegration.user.list.GithubFollowersUsersDataSource;
import com.alorma.githubintegration.user.list.GithubFollowingUsersDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosFollowersUsersClient
        implements GitskariosFactory<GithubClient<List<User>>, List<User>, List<GitskariosUser>> {

    private final Context context;
    private final String login;
    private int page = 0;

    public GitskariosFollowersUsersClient(Context context, String login) {
        this.context = context;
        this.login = login;
    }

    public GitskariosFollowersUsersClient(Context context, String login, int page) {
        this(context, login);
        this.page = page;
    }

    @Override
    public BaseDataSource<GithubClient<List<User>>, List<User>, List<GitskariosUser>> create() {
        GithubFollowersUsersDataSource githubFollowingUsersDataSource = new GithubFollowersUsersDataSource(context, login);
        if (page != 0) {
            githubFollowingUsersDataSource.setPage(page);
        }
        return githubFollowingUsersDataSource;
    }
}
