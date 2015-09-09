package com.alorma.githubintegration.repo.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.search.RepoSearchClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GithubSearchRepositoriesDataSource
        extends BaseDataSource<List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private String query;
    private int page;

    public GithubSearchRepositoriesDataSource(Context context, String query) {
        this.context = context;
        this.query = query;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosRepository>> callback) {
        RepoSearchClient client;
        if (page == 0) {
            client = new RepoSearchClient(context, query);
        } else {
            client = new RepoSearchClient(context, query, page);
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
