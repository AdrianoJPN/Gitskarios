package com.alorma.github.ui.fragment.repos;

import com.alorma.data.repos.list.GitskariosOrganizationsRepositoriesClient;
import com.alorma.github.R;
import com.alorma.github.ui.listeners.TitleProvider;

public class ReposFragmentFromOrgs extends BaseReposListFragment implements TitleProvider {

    public static ReposFragmentFromOrgs newInstance() {
        return new ReposFragmentFromOrgs();
    }

    @Override
    protected void loadArguments() {

    }

    @Override
    protected void executeRequest() {
        super.executeRequest();
        new GitskariosOrganizationsRepositoriesClient(getActivity()).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        new GitskariosOrganizationsRepositoriesClient(getActivity(), page).create().executeAsync(this);
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_repositories;
    }

    @Override
    public int getTitle() {
        return R.string.navigation_from_orgs;
    }
}
