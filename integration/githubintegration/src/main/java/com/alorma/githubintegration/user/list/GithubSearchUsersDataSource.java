package com.alorma.githubintegration.user.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.search.UsersSearchClient;
import com.alorma.githubintegration.mapper.user.list.ListUserMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

public class GithubSearchUsersDataSource
        extends BaseDataSource<List<GitskariosUser>>
        implements Paginated {

    private Context context;
    private String query;
    private int page;

    public GithubSearchUsersDataSource(Context context, String query) {
        this.context = context;
        this.query = query;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosUser>> callback) {
        UsersSearchClient client;
        if (page == 0) {
            client = new UsersSearchClient(context, query);
        } else {
            client = new UsersSearchClient(context, query, page);
        }
        client.setOnResultCallback(new BaseMapperCallback<List<User>, List<GitskariosUser>>(callback) {
            @Override
            protected BaseMapper<List<User>, List<GitskariosUser>> getMapper() {
                return new ListUserMapper();
            }
        });
        client.execute();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
