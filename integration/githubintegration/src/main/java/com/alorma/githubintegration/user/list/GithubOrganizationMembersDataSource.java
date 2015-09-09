package com.alorma.githubintegration.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.orgs.OrgsMembersClient;
import com.alorma.githubintegration.mapper.user.list.ListUserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class GithubOrganizationMembersDataSource
        extends BaseDataSource<List<GitskariosUser>>
        implements Paginated {

    private Context context;
    private String organization;
    private int page;

    public GithubOrganizationMembersDataSource(Context context, String organization) {
        this.context = context;
        this.organization = organization;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosUser>> callback) {
        OrgsMembersClient client;
        if (page == 0) {
            client = new OrgsMembersClient(context, organization);
        } else {
            client = new OrgsMembersClient(context, organization, page);
        }

        client.setOnResultCallback(new BaseMapperCallback<List<User>, List<GitskariosUser>>(callback) {
            @Override
            protected BaseMapper<List<User>, List<GitskariosUser>> getMapper() {
                return new ListUserMapper();
            }
        });
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
