package com.alorma.github;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.sdk.utils.GitskariosSettings;
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

    @Override
    public void onCreate() {
        super.onCreate();

        Log.wtf("GITINTERCEPT", "Application enter");

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker tracker = analytics.newTracker(R.xml.global_tracker);
            tracker.enableAdvertisingIdCollection(true);
        }

        GithubDeveloperCredentials.init(new SimpleDeveloperCredentialsProvider(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, BuildConfig.CLIENT_CALLBACK));

        JodaTimeAndroid.init(this);

        ImageLoader.getInstance().init(UniversalImageLoaderUtils.getImageLoaderConfiguration(this));

        ComponentName componentName = new ComponentName(this, Interceptor.class);
        int componentEnabledSetting = getPackageManager().getComponentEnabledSetting(componentName);

        GitskariosSettings settings = new GitskariosSettings(this);
        boolean interceptState = settings.interceptState(componentEnabledSetting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED);

        int flag = interceptState ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        getPackageManager().setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP);

    }

    public static GitskariosApplication get(Context context) {
        return (GitskariosApplication) context.getApplicationContext();
    }

}
