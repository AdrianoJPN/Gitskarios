package com.alorma.github.ui.fragment.repos;

import com.alorma.data.repos.list.GitskariosContributedRepositoriesClient;
import com.alorma.github.R;

public class ContributedReposFragment extends BaseReposListFragment {

    public static ContributedReposFragment newInstance() {
        return new ContributedReposFragment();
    }

    @Override
    protected void loadArguments() {

    }

    @Override
    protected void executeRequest() {
        super.executeRequest();
        new GitskariosContributedRepositoriesClient(getActivity()).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        new GitskariosContributedRepositoriesClient(getActivity(), page).create().executeAsync(this);
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_member_repositories;
    }
}
