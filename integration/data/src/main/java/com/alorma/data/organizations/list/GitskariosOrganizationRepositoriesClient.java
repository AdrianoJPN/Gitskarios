package com.alorma.data.organizations.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.organization.list.GithubOrganizationRepositoriesDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class GitskariosOrganizationRepositoriesClient
        implements GitskariosFactory<List<GitskariosRepository>> {

    private final Context context;
    private String organizations;
    private int page = 0;

    public GitskariosOrganizationRepositoriesClient(Context context, String organizations) {
        this.context = context;
        this.organizations = organizations;
    }

    public GitskariosOrganizationRepositoriesClient(Context context, String organizations, int page) {
        this(context, organizations);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosRepository>> create() {
        GithubOrganizationRepositoriesDataSource githubUserRepositoriesDataSource = new GithubOrganizationRepositoriesDataSource(context, organizations);
        if (page != 0) {
            githubUserRepositoriesDataSource.setPage(page);
        }
        return githubUserRepositoriesDataSource;
    }

    @Override
    public GitskariosFactory<List<GitskariosRepository>> setApiConnection(ApiConnection apiConnection) {
        return null;
    }

}
