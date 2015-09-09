package com.alorma.gitskarios.core;

import android.net.Uri;

/**
 * Created by Bernat on 19/04/2015.
 */
public interface ApiConnection {

    String getApiOauthUrlEndpoint();
    String getApiOauthRequest();
    String getApiEndpoint();
    String getType();

    Uri buildUri(Uri callbackUri);
}
