package com.alorma.gitlabsdk.security;

import android.net.Uri;

import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.GitskariosDeveloperCredentials;

/**
 * Created by a557114 on 08/09/2015.
 */
public class Gitlab implements ApiConnection {
    @Override
    public String getApiOauthUrlEndpoint() {
        return "http://gitlab.com";
    }

    @Override
    public String getApiOauthRequest() {
        return "http://gitlab.com/oauth/authorize";
    }

    @Override
    public String getApiEndpoint() {
        return "http://gitlab.com/api/v3";
    }

    @Override
    public String getType() {
        return "gitlab";
    }

    @Override
    public Uri buildUri(Uri callbackUri) {
        String url = String.format("%s?client_id=%s",
                getApiOauthRequest(),
                GitskariosDeveloperCredentials.getInstance().getProvider(this).getApiClient());
        return Uri.parse(url).buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("redirect_uri", callbackUri.toString())
                .build();
    }
}
