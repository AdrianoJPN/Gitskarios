package com.alorma.gitlabsdk.security.service.project;

import com.alorma.gitlabsdk.security.bean.GitlabProject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by a557114 on 09/09/2015.
 */
public interface ProjectsService {

    @GET("/projects/owned?order_by=last_activity_at")
    void projectsOwned(Callback<List<GitlabProject>> callback);

    @GET("/projects/owned?order_by=last_activity_at")
    void projectsOwned(@Query("page") int page, Callback<List<GitlabProject>> callback);


    @GET("/projects/owned?order_by=last_activity_at")
    List<GitlabProject> projectsOwned();

    @GET("/projects/owned?order_by=last_activity_at")
    List<GitlabProject> projectsOwned(@Query("page") int page);

}
