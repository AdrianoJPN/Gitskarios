package com.alorma.gitlabsdk.security.service.project;

import com.alorma.gitlabsdk.security.bean.GitlabProject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by a557114 on 13/09/2015.
 */
public interface ProjectService {

    @GET("/projects/{id}")
    void projectById(@Path("id") int id, Callback<GitlabProject> callback);

    @GET("/projects/{id}")
    GitlabProject projectById(@Path("id") int id);
}
