package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.repo.list.GithubSearchRepositoriesDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GitskariosSearchRepositoriesClient
        implements GitskariosFactory<List<GitskariosRepository>> {

    private final Context context;
    private final String query;
    private int page = 0;

    public GitskariosSearchRepositoriesClient(Context context, String query) {
        this.context = context;
        this.query = query;
    }

    public GitskariosSearchRepositoriesClient(Context context, String query, int page) {
        this(context, query);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosRepository>> create() {
        GithubSearchRepositoriesDataSource githubFollowingUsersDataSource = new GithubSearchRepositoriesDataSource(context, query);
        if (page != 0) {
            githubFollowingUsersDataSource.setPage(page);
        }
        return githubFollowingUsersDataSource;
    }

    @Override
    public GitskariosFactory<List<GitskariosRepository>> setApiConnection(ApiConnection apiConnection) {
        return null;
    }

}
