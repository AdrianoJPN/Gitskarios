package com.alorma.gitlabintegration.project;

import android.content.Context;

import com.alorma.gitlabintegration.mapper.ListReposMapper;
import com.alorma.gitlabintegration.mapper.UserMapper;
import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;
import com.alorma.gitlabsdk.security.service.project.GitlabProjectsOwnedClient;
import com.alorma.gitlabsdk.security.service.user.GitlabRequestAuthUser;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Paginated;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import java.util.List;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitlabProjectsOwnedDataSource extends BaseDataSource<List<GitskariosRepository>>
        implements Paginated {
    private final Context context;
    private int page;

    public GitlabProjectsOwnedDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void executeAsync(Callback<List<GitskariosRepository>> callback) {
        GitlabProjectsOwnedClient requestUserClient;

        if (page == 0) {
            requestUserClient = new GitlabProjectsOwnedClient(context);
        } else {
            requestUserClient = new GitlabProjectsOwnedClient(context, page);
        }

        requestUserClient.setOnResultCallback(
                new BaseMapperCallback<List<GitlabProject>, List<GitskariosRepository>>(callback) {
            @Override
            protected BaseMapper<List<GitlabProject>, List<GitskariosRepository>> getMapper() {
                return new ListReposMapper();
            }
        });
        requestUserClient.execute();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
