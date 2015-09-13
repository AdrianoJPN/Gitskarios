package com.alorma.github.ui.presenter;

import com.alorma.github.R;
import com.alorma.github.ui.fragment.detail.repo.BackManager;
import com.alorma.gitskarios.core.ApiConnection;

/**
 * Created by a557114 on 13/09/2015.
 */
public class MainPresenter implements BackManager {

    private MainPresenterCallback mainPresenterCallback;

    private ApiConnection apiConnection;

    public boolean isEventsEnabled() {
        return apiConnection.getType().equals("github");
    }

    public boolean isRepositoriesEnabled() {
        return true;
    }

    public boolean isPeopleEnabled() {
        return apiConnection.getType().equals("github");
    }

    public boolean isGistEnabled() {
        return apiConnection.getType().equals("github");
    }

    public boolean isNotificationsEnabled() {
        return apiConnection.getType().equals("github");
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

    public int getSelectedDrawerItem() {
        return apiConnection.getType().equals("github") ? R.id.nav_drawer_events : R.id.nav_drawer_repositories;
    }

    @Override
    public boolean onBackPressed() {
        return !apiConnection.getType().equals("github");
    }

    public interface MainPresenterCallback {
        void createDrawer();
    }
}
