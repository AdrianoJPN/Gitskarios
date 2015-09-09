package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.githubintegration.repo.list.GithubForkRepositoriesDataSource;
import com.alorma.githubintegration.repo.list.GithubStarredRepositoriesDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.client.BaseClient;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosForkRepositoriesClient
        implements GitskariosFactory<List<GitskariosRepository>> {

    private final Context context;
    private final RepoInfo repoInfo;
    private int page = 0;

    public GitskariosForkRepositoriesClient(Context context, RepoInfo repoInfo) {
        this.context = context;
        this.repoInfo = repoInfo;
    }

    public GitskariosForkRepositoriesClient(Context context, RepoInfo repoInfo, int page) {
        this(context, repoInfo);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosRepository>> create() {
        GithubForkRepositoriesDataSource githubUserRepositoriesDataSource = new GithubForkRepositoriesDataSource(context, repoInfo);
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
