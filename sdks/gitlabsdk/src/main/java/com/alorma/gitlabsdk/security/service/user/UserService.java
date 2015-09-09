package com.alorma.gitlabsdk.security.service.user;

import com.alorma.gitlabsdk.security.bean.dto.GitlabUser;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by a557114 on 09/09/2015.
 */
public interface UserService {

    // Async
    @GET("/user")
    void me(Callback<GitlabUser> callback);

    @GET("/user/{id}")
    void getSingleUser(@Query("id") String id, Callback<GitlabUser> callback);

    // Sync
    @GET("/user")
    GitlabUser me();

    @GET("/user/{id}")
    GitlabUser getSingleUser(@Query("id") String id);
}
