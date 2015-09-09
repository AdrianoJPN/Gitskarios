package com.alorma.data.repos.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.security.GitHub;
import com.alorma.githubintegration.repo.list.GithubUserRepositoriesDataSource;
import com.alorma.gitlabintegration.project.GitlabProjectsOwnedDataSource;
import com.alorma.gitlabsdk.security.Gitlab;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GitskariosUserRepositoriesClient
        implements GitskariosFactory<List<GitskariosRepository>> {

    private final Context context;
    private final String username;
    private int page = 0;
    private ApiConnection apiConnection;

    public GitskariosUserRepositoriesClient(Context context, String username) {
        this.context = context;
        this.username = username;

    }

    public GitskariosUserRepositoriesClient(Context context, String username, int page) {
        this(context, username);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosRepository>> create() {
        if (apiConnection != null) {
            if (apiConnection.getType().equals(new GitHub().getType())) {
                GithubUserRepositoriesDataSource githubUserRepositoriesDataSource = new GithubUserRepositoriesDataSource(context, username);
                if (page != 0) {
                    githubUserRepositoriesDataSource.setPage(page);
                }
                return githubUserRepositoriesDataSource;
            } else if (apiConnection.getType().equals(new Gitlab().getType())) {
                GitlabProjectsOwnedDataSource gitlabProjectsOwnedDataSource = new GitlabProjectsOwnedDataSource(context);
                if (page != 0) {
                    gitlabProjectsOwnedDataSource.setPage(page);
                }
                return gitlabProjectsOwnedDataSource;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public GitskariosFactory<List<GitskariosRepository>> setApiConnection(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
        return this;
    }

}
