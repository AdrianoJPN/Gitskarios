package com.alorma.github.ui.presenter;

import com.alorma.gitskarios.core.ApiConnection;

/**
 * Created by a557114 on 13/09/2015.
 */
public class MainPresenter {

    private MainPresenterCallback mainPresenterCallback;

    private ApiConnection apiConnection;

    public boolean isEventsEnabled() {
        return false;
    }

    public boolean isRepositoriesEnabled() {
        return false;
    }

    public boolean isPeopleEnabled() {
        return false;
    }

    public boolean isGistEnabled() {
        return false;
    }

    public boolean isNotificationsEnabled() {
        return false;
    }

    public void setApiConnection(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
        if (mainPresenterCallback != null) {
            mainPresenterCallback.createDrawer();
        }
    }

    public ApiConnection getApiConnection() {
        return apiConnection;
    }

    public void setMainPresenterCallback(MainPresenterCallback mainPresenterCallback) {
        this.mainPresenterCallback = mainPresenterCallback;
    }

    public interface MainPresenterCallback {
        void createDrawer();
    }
}
