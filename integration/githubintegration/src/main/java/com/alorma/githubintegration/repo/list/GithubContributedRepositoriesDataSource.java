package com.alorma.githubintegration.repo.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.repos.MemberReposClient;
import com.alorma.github.sdk.services.repos.UserReposFromOrganizationClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubContributedRepositoriesDataSource
        extends BaseDataSource<GithubClient<List<Repo>>, List<Repo>, List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private int page;

    public GithubContributedRepositoriesDataSource(Context context) {
        this.context = context;
    }

    @Override
    public GithubClient<List<Repo>> getApiClient() {
        if (page == 0) {
            return new MemberReposClient(context);
        } else {
            return new MemberReposClient(context, page);
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
