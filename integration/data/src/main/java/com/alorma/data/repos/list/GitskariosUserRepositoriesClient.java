package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.repo.list.GithubUserRepositoriesDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GitskariosUserRepositoriesClient
        implements GitskariosFactory<List<Repo>, List<GitskariosRepository>> {

    private final Context context;
    private final String username;
    private int page = 0;

    public GitskariosUserRepositoriesClient(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public GitskariosUserRepositoriesClient(Context context, String username, int page) {
        this(context, username);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<Repo>, List<GitskariosRepository>> create() {
        GithubUserRepositoriesDataSource githubUserRepositoriesDataSource = new GithubUserRepositoriesDataSource(context, username);
        if (page != 0) {
            githubUserRepositoriesDataSource.setPage(page);
        }
        return githubUserRepositoriesDataSource;
    }
}
