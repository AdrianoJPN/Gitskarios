package com.alorma.github.ui.fragment.orgs;

import android.os.Bundle;

import com.alorma.data.user.list.GitskariosOrganizationMembersClient;
import com.alorma.github.R;
import com.alorma.github.ui.fragment.users.BaseUsersListFragment;

public class OrgsMembersFragment extends BaseUsersListFragment {
    private String org;

    public static OrgsMembersFragment newInstance() {
        return new OrgsMembersFragment();
    }

    public static OrgsMembersFragment newInstance(String org) {
        OrgsMembersFragment orgsMembersFragment = new OrgsMembersFragment();
        if (org != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, org);

            orgsMembersFragment.setArguments(bundle);
        }
        return orgsMembersFragment;
    }

    @Override
    protected void executeRequest() {
        new GitskariosOrganizationMembersClient(getActivity(), org).create().executeAsync(this);
    }

    @Override
    protected void executePaginatedRequest(int page) {
        new GitskariosOrganizationMembersClient(getActivity(), org, page).create().executeAsync(this);
    }

    @Override
    protected void loadArguments() {
        if (getArguments() != null) {
            org = getArguments().getString(USERNAME);
        }
    }

    @Override
    protected int getNoDataText() {
        return R.string.org_no_members;
    }
}
