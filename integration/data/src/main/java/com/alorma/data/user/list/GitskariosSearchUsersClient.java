package com.alorma.data.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.githubintegration.user.list.GithubFollowersUsersDataSource;
import com.alorma.githubintegration.user.list.GithubSearchUsersDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosSearchUsersClient
        implements GitskariosFactory<GithubClient<List<User>>, List<User>, List<GitskariosUser>> {

    private final Context context;
    private final String query;
    private int page = 0;

    public GitskariosSearchUsersClient(Context context, String query) {
        this.context = context;
        this.query = query;
    }

    public GitskariosSearchUsersClient(Context context, String query, int page) {
        this(context, query);
        this.page = page;
    }

    @Override
    public BaseDataSource<GithubClient<List<User>>, List<User>, List<GitskariosUser>> create() {
        GithubSearchUsersDataSource githubFollowingUsersDataSource = new GithubSearchUsersDataSource(context, query);
        if (page != 0) {
            githubFollowingUsersDataSource.setPage(page);
        }
        return githubFollowingUsersDataSource;
    }
}
