package com.alorma.gitlabsdk.security;

import com.alorma.gitskarios.core.ApiConnection;

/**
 * Created by a557114 on 08/09/2015.
 */
public class Gitlab implements ApiConnection {
    @Override
    public String getApiOauthUrlEndpoint() {
        return null;
    }

    @Override
    public String getApiOauthRequest() {
        return null;
    }

    @Override
    public String getApiEndpoint() {
        return null;
    }

    @Override
    public String getType() {
        return "gitlab";
    }
}
