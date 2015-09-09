package com.alorma.githubintegration.repo.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.repos.UserReposFromOrganizationClient;
import com.alorma.github.sdk.services.repos.WatchedReposClient;
import com.alorma.github.sdk.services.search.RepoSearchClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubOrganizationsRepositoriesDataSource
        extends BaseDataSource<List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private int page;

    public GithubOrganizationsRepositoriesDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosRepository>> callback) {
        UserReposFromOrganizationClient client;
        if (page == 0) {
            client = new UserReposFromOrganizationClient(context);
        } else {
            client = new UserReposFromOrganizationClient(context, page);
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
