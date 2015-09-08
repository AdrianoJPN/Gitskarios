package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;

import com.alorma.data.repos.list.GitskariosForkRepositoriesClient;
import com.alorma.github.R;
import com.alorma.github.sdk.bean.info.RepoInfo;

public class ListForksFragment extends BaseReposListFragment {

    private static final String REPO_INFO = "REPO_INFO";
    private RepoInfo repoInfo;

    public static ListForksFragment newInstance(RepoInfo repoInfo) {
        ListForksFragment reposFragment = new ListForksFragment();
        if (repoInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(REPO_INFO, repoInfo);

            reposFragment.setArguments(bundle);
        }
        return reposFragment;
    }

    @Override
    protected void executeRequest() {
        super.executeRequest();
        if (repoInfo != null) {
            new GitskariosForkRepositoriesClient(getActivity(), repoInfo).create().executeAsync(this);
        }
    }

    @Override
    protected void executePaginatedRequest(int page) {
        super.executePaginatedRequest(page);
        if (repoInfo != null) {
            new GitskariosForkRepositoriesClient(getActivity(), repoInfo, page).create().executeAsync(this);
        }
    }

    @Override
    protected int getNoDataText() {
        return R.string.no_forked_repositories;
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            repoInfo = getArguments().getParcelable(REPO_INFO);
        }
    }

    @Override
    protected boolean showAdapterOwnerName() {
        return true;
    }
}
