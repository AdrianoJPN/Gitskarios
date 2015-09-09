package com.alorma.data.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.githubintegration.user.list.GithubOrganizationMembersDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class GitskariosOrganizationMembersClient
        implements GitskariosFactory<List<GitskariosUser>> {

    private final Context context;
    private final String organization;
    private int page = 0;

    public GitskariosOrganizationMembersClient(Context context, String organization) {
        this.context = context;
        this.organization = organization;
    }

    public GitskariosOrganizationMembersClient(Context context, String organization, int page) {
        this(context, organization);
        this.page = page;
    }

    @Override
    public BaseDataSource<List<GitskariosUser>> create() {
        GithubOrganizationMembersDataSource githubOrganizationMembersDataSource =
                new GithubOrganizationMembersDataSource(context, organization);
        if (page != 0) {
            githubOrganizationMembersDataSource.setPage(page);
        }
        return githubOrganizationMembersDataSource;
    }

    @Override
    public GitskariosFactory<List<GitskariosUser>> setApiConnection(ApiConnection apiConnection) {
        return null;
    }
}
