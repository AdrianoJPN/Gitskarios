package com.alorma.data;

import android.content.Context;

import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.security.GitHub;
import com.alorma.githubintegration.repo.GithubRepositoryDataSource;
import com.alorma.gitlabintegration.project.GitlabProjectDataSource;
import com.alorma.gitlabsdk.security.Gitlab;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

/**
 * Created by a557114 on 13/09/2015.
 */
public class GitskariosRepositoryClient implements GitskariosFactory<GitskariosRepository> {
    private ApiConnection apiConnection;

    private Context context;
    private RepoInfo repoInfo;

    public GitskariosRepositoryClient(Context context, RepoInfo repoInfo) {
        this.context = context;
        this.repoInfo = repoInfo;
    }

    @Override
    public BaseDataSource<GitskariosRepository> create() {
        if (apiConnection != null) {
            if (apiConnection.getType().equals(new GitHub().getType())) {
                return new GithubRepositoryDataSource(context, repoInfo);
            } else if (apiConnection.getType().equals(new Gitlab().getType())) {
                return new GitlabProjectDataSource(context, repoInfo.repo_id);
            }
        }
        return null;
    }

    @Override
    public GitskariosFactory<GitskariosRepository> setApiConnection(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
        return this;
    }
}
