package com.alorma.github.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.alorma.github.R;

import java.util.Random;

/**
 * Created by Bernat on 21/09/2015.
 */
public class RepoStarsScreenWidget extends AppWidgetProvider {

    public RepoStarsScreenWidget() {
        super();
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateWidgets(context, appWidgetManager);
    }

    private void updateWidgets(Context context, AppWidgetManager appWidgetManager) {
        ComponentName thisWidget = new ComponentName(context,
                RepoStarsScreenWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout_repo_starts);

            remoteViews.setTextViewText(R.id.widget_repo_stars_repo_name, "gitskarios");
            remoteViews.setTextViewText(R.id.widget_repo_stars_repo_stars, "127");
            remoteViews.setTextViewText(R.id.widget_repo_stars_repo_watchers, "9");
            remoteViews.setTextViewText(R.id.widget_repo_stars_repo_forks, "41");

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
