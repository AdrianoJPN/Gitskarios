package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.repo.list.GithubOrganizationsRepositoriesDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GitskariosOrganizationsRepositoriesClient
        implements GitskariosFactory<List<GitskariosRepository>> {

    private final Context context;
    private int page = 0;

    public GitskariosOrganizationsRepositoriesClient(Context context) {
        this.context = context;
    }

    public GitskariosOrganizationsRepositoriesClient(Context context, int page) {
        this(context);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosRepository>> create() {
        GithubOrganizationsRepositoriesDataSource githubUserRepositoriesDataSource = new GithubOrganizationsRepositoriesDataSource(context);
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
