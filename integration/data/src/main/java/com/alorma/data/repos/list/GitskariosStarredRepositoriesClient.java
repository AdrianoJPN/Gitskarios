package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.repo.list.GithubStarredRepositoriesDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GitskariosStarredRepositoriesClient
        implements GitskariosFactory<List<GitskariosRepository>> {

    private final Context context;
    private final String username;
    private int page = 0;

    public GitskariosStarredRepositoriesClient(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public GitskariosStarredRepositoriesClient(Context context, String username, int page) {
        this(context, username);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosRepository>> create() {
        GithubStarredRepositoriesDataSource githubUserRepositoriesDataSource = new GithubStarredRepositoriesDataSource(context, username);
        if (page != 0) {
            githubUserRepositoriesDataSource.setPage(page);
        }
        return githubUserRepositoriesDataSource;
    }

    @Override
    public GitskariosFactory<List<GitskariosRepository>> setApiConnection(ApiConnection apiConnection) {
        return null;
    }

}
