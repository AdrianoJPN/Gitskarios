package com.alorma.gitlabsdk.security.service.project.list;

import android.content.Context;

import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitlabsdk.security.client.GitlabClient;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitlabProjectsOwnedClient extends GitlabClient<List<GitlabProject>> {
    private int page;

    public GitlabProjectsOwnedClient(Context context) {
        super(context);
    }
    public GitlabProjectsOwnedClient(Context context, int page) {
        this(context);
        this.page = page;
    }

    @Override
    protected void executeService(RestAdapter restAdapter) {
        if (page == 0) {
            restAdapter.create(ProjectsService.class).projectsOwned(this);
        } else {
            restAdapter.create(ProjectsService.class).projectsOwned(page, this);
        }
    }

    @Override
    protected List<GitlabProject> executeServiceSync(RestAdapter restAdapter) {
        if (page == 0) {
            return restAdapter.create(ProjectsService.class).projectsOwned();
        } else {
            return restAdapter.create(ProjectsService.class).projectsOwned(page);
        }
    }
}
