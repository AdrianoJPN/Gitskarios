package com.alorma.github.ui.fragment.orgs;

import android.os.Bundle;

import com.alorma.data.organizations.list.GitskariosOrganizationRepositoriesClient;
import com.alorma.github.R;
import com.alorma.github.ui.fragment.repos.BaseReposListFragment;

public class OrgsReposFragment extends BaseReposListFragment {

    private String org;

    private static final String ORGANIZATION = "ORG";

    public static OrgsReposFragment newInstance() {
        return new OrgsReposFragment();
    }

    public static OrgsReposFragment newInstance(String orgName) {
        OrgsReposFragment reposFragment = new OrgsReposFragment();
        if (orgName != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ORGANIZATION, orgName);

            reposFragment.setArguments(bundle);
        }
        return reposFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            org = getArguments().getString(ORGANIZATION);
        }
    }

    @Override
    protected void loadArguments() {

    }

    @Override
    protected void executeRequest() {
        super.executeRequest();
        new GitskariosOrganizationRepositoriesClient(getActivity(), org).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        new GitskariosOrganizationRepositoriesClient(getActivity(), org, page).create().executeAsync(this);
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_repositories;
    }

}
