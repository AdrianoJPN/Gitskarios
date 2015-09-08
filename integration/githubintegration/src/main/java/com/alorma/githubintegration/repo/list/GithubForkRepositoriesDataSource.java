package com.alorma.githubintegration.repo.list;

import android.content.Context;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.repo.GetForksClient;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GithubForkRepositoriesDataSource
        extends BaseDataSource<List<Repo>, List<GitskariosRepository>>
        implements Paginated {

    private Context context;
    private RepoInfo repoInfo;
    private int page;

    public GithubForkRepositoriesDataSource(Context context, RepoInfo repoInfo) {
        this.context = context;
        this.repoInfo = repoInfo;
    }

    @Override
    public GithubClient<List<Repo>> getApiClient() {
        GetForksClient client;
        if (page == 0) {
            client = new GetForksClient(context, repoInfo);
        } else {
            client = new GetForksClient(context, repoInfo, page);
        }

        client.setSort(GetForksClient.STARGAZERS);
        return client;
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
