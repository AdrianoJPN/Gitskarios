package com.alorma.githubintegration.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.search.UsersSearchClient;
import com.alorma.githubintegration.mapper.user.list.ListUserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubSearchUsersDataSource
        extends BaseDataSource<GithubClient<List<User>>, List<User>, List<GitskariosUser>>
        implements Paginated {

    private Context context;
    private String query;
    private int page;

    public GithubSearchUsersDataSource(Context context, String query) {
        this.context = context;
        this.query = query;
    }

    @Override
    public GithubClient<List<User>> getApiClient() {
        if (page == 0) {
            return new UsersSearchClient(context, query);
        } else {
            return new UsersSearchClient(context, query, page);
        }
    }

    @Override
    public BaseMapper<List<User>, List<GitskariosUser>> getMapper() {
        return new ListUserMapper();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
