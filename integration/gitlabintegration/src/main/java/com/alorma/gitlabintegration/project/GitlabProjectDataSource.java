package com.alorma.gitlabintegration.project;

import android.content.Context;

import com.alorma.gitlabintegration.mapper.RepoMapper;
import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitlabsdk.security.service.project.GitlabGetProjectClient;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

/**
 * Created by a557114 on 13/09/2015.
 */
public class GitlabProjectDataSource extends BaseDataSource<GitskariosRepository> {

    private final Context context;
    private int id;

    public GitlabProjectDataSource(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public void executeAsync(Callback<GitskariosRepository> callback) {
        GitlabGetProjectClient repoClient = new GitlabGetProjectClient(context, id);
        repoClient.setOnResultCallback(new BaseMapperCallback<GitlabProject, GitskariosRepository>(callback) {
            @Override
            protected BaseMapper<GitlabProject, GitskariosRepository> getMapper() {
                return new RepoMapper();
            }
        });
        repoClient.execute();
    }
}
