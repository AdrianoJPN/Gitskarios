package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;

import com.alorma.data.repos.list.GitskariosUserRepositoriesClient;
import com.alorma.github.GitskariosApplication;
import com.alorma.github.R;
import com.alorma.github.ui.listeners.TitleProvider;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.List;

public class ReposFragment extends BaseReposListFragment implements TitleProvider{

    private String username;

    public static ReposFragment newInstance() {
        return new ReposFragment();
    }

    public static ReposFragment newInstance(String username) {
        ReposFragment reposFragment = new ReposFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            reposFragment.setArguments(bundle);
        }
        return reposFragment;
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    protected void executeRequest() {
        super.executeRequest();

        ApiConnection apiConnection = ((GitskariosApplication) getActivity().getApplicationContext()).getApiConnection();

        BaseDataSource<List<GitskariosRepository>> dataSource = new GitskariosUserRepositoriesClient(getActivity(), username)
                .setApiConnection(apiConnection)
                .create();
        if (dataSource != null) {
            dataSource.executeAsync(this);
        }
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        ApiConnection apiConnection = ((GitskariosApplication) getActivity().getApplicationContext()).getApiConnection();

        BaseDataSource<List<GitskariosRepository>> dataSource = new GitskariosUserRepositoriesClient(getActivity(), username, page)
                .setApiConnection(apiConnection)
                .create();
        if (dataSource != null) {
            dataSource.executeAsync(this);
        }
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_repositories;
    }

    @Override
    public int getTitle() {
        return R.string.navigation_repos;
    }
}
