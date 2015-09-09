package com.alorma.githubintegration.repo.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.repos.WatchedReposClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GithubWatchedRepositoriesDataSource
        extends BaseDataSource<List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private String username;
    private int page;

    public GithubWatchedRepositoriesDataSource(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosRepository>> callback) {
        WatchedReposClient client;
        if (page == 0) {
            client = new WatchedReposClient(context, username);
        } else {
            client = new WatchedReposClient(context, username, page);
        }
        client.setOnResultCallback(new BaseMapperCallback<List<Repo>, List<GitskariosRepository>>(callback) {
            @Override
            protected BaseMapper<List<Repo>, List<GitskariosRepository>> getMapper() {
                return new ListRepositoryMapper();
            }
        });
        client.execute();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
