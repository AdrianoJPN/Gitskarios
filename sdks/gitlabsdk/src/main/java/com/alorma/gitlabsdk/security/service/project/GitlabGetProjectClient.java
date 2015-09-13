package com.alorma.gitlabsdk.security.service.project;

import android.content.Context;

import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitlabsdk.security.client.GitlabClient;

import retrofit.RestAdapter;

/**
 * Created by a557114 on 13/09/2015.
 */
public class GitlabGetProjectClient extends GitlabClient<GitlabProject> {

    private int repoId;

    public GitlabGetProjectClient(Context context, int repoId) {
        super(context);
        this.repoId = repoId;
    }

    @Override
    protected void executeService(RestAdapter restAdapter) {
        restAdapter.create(ProjectService.class).projectById(repoId, this);
    }

    @Override
    protected GitlabProject executeServiceSync(RestAdapter restAdapter) {
        return restAdapter.create(ProjectService.class).projectById(repoId);
    }
}
