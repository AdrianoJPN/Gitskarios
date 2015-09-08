package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.githubintegration.repo.list.GithubContributedRepositoriesDataSource;
import com.alorma.githubintegration.repo.list.GithubOrganizationsRepositoriesDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.client.BaseClient;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosContributedRepositoriesClient
        implements GitskariosFactory<List<Repo>, List<GitskariosRepository>> {

    private final Context context;
    private int page = 0;

    public GitskariosContributedRepositoriesClient(Context context) {
        this.context = context;
    }

    public GitskariosContributedRepositoriesClient(Context context, int page) {
        this(context);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<Repo>, List<GitskariosRepository>> create() {
        GithubContributedRepositoriesDataSource githubUserRepositoriesDataSource = new GithubContributedRepositoriesDataSource(context);
        if (page != 0) {
            githubUserRepositoriesDataSource.setPage(page);
        }
        return githubUserRepositoriesDataSource;
    }
}
