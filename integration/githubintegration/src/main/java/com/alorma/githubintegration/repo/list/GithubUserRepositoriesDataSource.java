package com.alorma.githubintegration.repo.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.repos.UserReposClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GithubUserRepositoriesDataSource
        extends BaseDataSource<List<Repo>, List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private String username;
    private int page;

    public GithubUserRepositoriesDataSource(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    @Override
    public GithubClient<List<Repo>> getApiClient() {
        if (page == 0) {
            return new UserReposClient(context, username);
        } else {
            return new UserReposClient(context, username, page);
        }
    }

    @Override
    public BaseMapper<List<Repo>, List<GitskariosRepository>> getMapper() {
        return new ListRepositoryMapper();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
