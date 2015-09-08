package com.alorma.gitskarios.core.client.credentials;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Bernat on 07/07/2015.
 */
public abstract class MetaDeveloperCredentialsProvider implements DeveloperCredentialsProvider {

    private String apiClient;
    private String apiSecret;
    private String apiOauth;

    public MetaDeveloperCredentialsProvider(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            apiClient = bundle.getString(getApiClientKey());
            apiSecret = bundle.getString(getApiSecretKey());
            apiOauth = bundle.getString(getApiCallbackKey());
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GitHubSdk", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("GitHubSdk", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    protected abstract String getApiClientKey();

    protected abstract String getApiSecretKey();

    protected abstract String getApiCallbackKey();

    @Override
    public String getApiClient() {
        return apiClient;
    }

    @Override
    public String getAPiSecret() {
        return apiSecret;
    }

    @Override
    public String getApiOauth() {
        return apiOauth;
    }

}
