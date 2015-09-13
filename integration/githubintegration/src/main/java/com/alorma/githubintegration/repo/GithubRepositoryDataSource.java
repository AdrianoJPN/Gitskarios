package com.alorma.githubintegration.repo;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.repo.GetRepoClient;
import com.alorma.githubintegration.mapper.repo.RepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

/**
 * Created by a557114 on 13/09/2015.
 */
public class GithubRepositoryDataSource extends BaseDataSource<GitskariosRepository> {

    private final Context context;
    private final RepoInfo repoInfo;

    public GithubRepositoryDataSource(Context context, RepoInfo repoInfo) {
        this.context = context;
        this.repoInfo = repoInfo;
    }

    @Override
    public void executeAsync(Callback<GitskariosRepository> callback) {
        GetRepoClient repoClient = new GetRepoClient(context, repoInfo);
        repoClient.setOnResultCallback(new BaseMapperCallback<Repo, GitskariosRepository>(callback) {
            @Override
            protected BaseMapper<Repo, GitskariosRepository> getMapper() {
                return new RepositoryMapper();
            }
        });
        repoClient.execute();
    }
}
