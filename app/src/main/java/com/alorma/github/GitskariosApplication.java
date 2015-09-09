package com.alorma.github;

import android.app.Application;
import android.content.Context;

import com.alorma.github.sdk.security.GitHub;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.GitskariosDeveloperCredentials;
import com.alorma.gitlabsdk.security.Gitlab;
import com.alorma.gitskarios.core.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.ui.utils.UniversalImageLoaderUtils;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Bernat on 08/07/2014.
 */
public class GitskariosApplication extends Application {

    private ApiConnection apiConnection;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker tracker = analytics.newTracker(R.xml.global_tracker);
            tracker.enableAdvertisingIdCollection(true);
        }

        GitskariosDeveloperCredentials.init(new GitHub(),
                new SimpleDeveloperCredentialsProvider(BuildConfig.GH_CLIENT_ID, BuildConfig.GH_CLIENT_SECRET, BuildConfig.GH_CLIENT_CALLBACK));

        GitskariosDeveloperCredentials.init(new Gitlab(),
                new SimpleDeveloperCredentialsProvider(BuildConfig.GLAB_CLIENT_ID, BuildConfig.GLAB_CLIENT_SECRET, BuildConfig.GLAB_CLIENT_CALLBACK));

        JodaTimeAndroid.init(this);

        ImageLoader.getInstance().init(UniversalImageLoaderUtils.getImageLoaderConfiguration(this));
    }

    public static GitskariosApplication get(Context context) {
        return (GitskariosApplication) context.getApplicationContext();
    }

    public void setApiConnection(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
    }

    public ApiConnection getApiConnection() {
        return apiConnection;
    }
}
