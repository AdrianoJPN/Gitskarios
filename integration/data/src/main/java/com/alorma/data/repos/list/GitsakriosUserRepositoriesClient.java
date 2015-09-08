package com.alorma.data.repos.list;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.githubintegration.repo.list.GithubUserRepositoriesDataSource;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitsakriosUserRepositoriesClient
        implements GitskariosFactory<GithubClient<List<Repo>>, List<Repo>, List<GitskariosRepository>> {

    private final Context context;
    private final String username;
    private int page = 0;

    public GitsakriosUserRepositoriesClient(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public GitsakriosUserRepositoriesClient(Context context, String username, int page) {
        this(context, username);
        this.page = page;
    }

    @Override
    public BaseDataSource<GithubClient<List<Repo>>, List<Repo>, List<GitskariosRepository>> create() {
        GithubUserRepositoriesDataSource githubUserRepositoriesDataSource = new GithubUserRepositoriesDataSource(context, username);
        if (page != 0) {
            githubUserRepositoriesDataSource.setPage(page);
        }
        return githubUserRepositoriesDataSource;
    }
}
