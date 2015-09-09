package com.alorma.githubintegration.organization.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.orgs.OrgsReposClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GithubOrganizationRepositoriesDataSource
        extends BaseDataSource<List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private String organization;
    private int page;

    public GithubOrganizationRepositoriesDataSource(Context context, String organization) {
        this.context = context;
        this.organization = organization;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosRepository>> callback) {
        OrgsReposClient client;
        if (page == 0) {
            client = new OrgsReposClient(context, organization);
        } else {
            client = new OrgsReposClient(context, organization, page);
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
