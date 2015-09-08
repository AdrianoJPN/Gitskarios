package com.alorma.github.ui.fragment.search;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alorma.data.user.list.GitskariosSearchUsersClient;
import com.alorma.github.R;
import com.alorma.github.ui.fragment.users.BaseUsersListFragment;
import com.mikepenz.octicons_typeface_library.Octicons;

public class SearchUsersFragment extends BaseUsersListFragment {

    private String query;

    public static SearchUsersFragment newInstance(String query) {
        Bundle args = new Bundle();
        if (query != null) {
            args.putString(SearchManager.QUERY, query);
        }
        SearchUsersFragment f = new SearchUsersFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setBackgroundColor(Color.WHITE);

        String query = getArguments().getString(SearchManager.QUERY, null);
        if (query != null) {
            setQuery(query);
        } else {
            setEmpty(false);
        }
    }

    public void setQuery(String query) {
        this.query = query;
        executeRequest();
    }

    @Override
    protected void loadArguments() {

    }

    @Override
    protected void executeRequest() {
        if (getActivity() != null) {
            if (query != null) {
                super.executeRequest();
                new GitskariosSearchUsersClient(getActivity(), query).create().executeAsync(this);
                query = null;
                if (getAdapter() != null) {
                    getAdapter().clear();
                }
            }
        }
    }

    @Override
    protected void executePaginatedRequest(int page) {
        if (getActivity() != null) {
            if (query != null) {
                super.executePaginatedRequest(page);
                new GitskariosSearchUsersClient(getActivity(), query, page).create().executeAsync(this);
                query = null;
                if (getAdapter() != null) {
                    getAdapter().clear();
                }
            }
        }
    }

    @Override
    protected Octicons.Icon getNoDataIcon() {
        return Octicons.Icon.oct_person;
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_results;
    }
}
