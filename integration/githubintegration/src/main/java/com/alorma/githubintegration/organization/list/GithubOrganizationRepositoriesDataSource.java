package com.alorma.githubintegration.organization.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.orgs.OrgsReposClient;
import com.alorma.github.sdk.services.repos.MemberReposClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubOrganizationRepositoriesDataSource
        extends BaseDataSource<GithubClient<List<Repo>>, List<Repo>, List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private String organization;
    private int page;

    public GithubOrganizationRepositoriesDataSource(Context context, String organization) {
        this.context = context;
        this.organization = organization;
    }

    @Override
    public GithubClient<List<Repo>> getApiClient() {
        if (page == 0) {
            return new OrgsReposClient(context, organization);
        } else {
            return new OrgsReposClient(context, organization, page);
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
