package com.alorma.github.ui.fragment.search;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alorma.data.repos.list.GitskariosSearchRepositoriesClient;
import com.alorma.github.R;
import com.alorma.github.ui.fragment.repos.BaseReposListFragment;
import com.mikepenz.octicons_typeface_library.Octicons;

/**
 * Created by Bernat on 08/08/2014.
 */
public class SearchReposFragment extends BaseReposListFragment {

    private String query = null;

    public static SearchReposFragment newInstance(String query) {
        Bundle args = new Bundle();
        if (query != null) {
            args.putString(SearchManager.QUERY, query);
        }
        SearchReposFragment f = new SearchReposFragment();
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

    @Override
    protected Octicons.Icon getNoDataIcon() {
        return Octicons.Icon.oct_repo;
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_results;
    }

    @Override
    protected void loadArguments() {

    }

    @Override
    protected void executeRequest() {
        if (getActivity() != null) {
            if (query != null) {
                super.executeRequest();
                new GitskariosSearchRepositoriesClient(getActivity(), query).create().executeAsync(this);
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
                new GitskariosSearchRepositoriesClient(getActivity(), query, page).create().executeAsync(this);
                query = null;
                if (getAdapter() != null) {
                    getAdapter().clear();
                }
            }
        }
    }

    public void setQuery(String query) {
        this.query = query;
        executeRequest();
    }
}
