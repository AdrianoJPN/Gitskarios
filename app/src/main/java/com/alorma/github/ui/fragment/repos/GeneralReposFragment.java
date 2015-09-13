package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alorma.github.GitskariosApplication;
import com.alorma.github.R;
import com.alorma.github.ui.fragment.base.BaseFragment;
import com.alorma.github.ui.listeners.TitleProvider;
import com.alorma.github.ui.presenter.GeneralReposPresenter;
import com.alorma.gitskarios.core.ApiConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bernat on 06/06/2015.
 */
public class GeneralReposFragment extends BaseFragment implements GeneralReposPresenter.GeneralReposPresenterCallback {

    private GeneralReposPresenter generalReposPresenter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static GeneralReposFragment newInstance() {
        return new GeneralReposFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.general_repos_fragment, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tabLayout = (TabLayout) view.findViewById(R.id.tabStrip);
        ViewCompat.setElevation(tabLayout, 4);

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        generalReposPresenter = new GeneralReposPresenter();

        ApiConnection apiConnection = ((GitskariosApplication) getActivity().getApplicationContext()).getApiConnection();
        generalReposPresenter.setGeneralReposPresenterCallback(this);
        generalReposPresenter.setApiConnection(apiConnection);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.navigation_general_repositories);
    }

    @Override
    public void createViewPager(List<Fragment> items) {
        if (viewPager != null && tabLayout != null) {
            ReposPagerAdapter reposAdapter = new ReposPagerAdapter(getChildFragmentManager(), items);
            viewPager.setOffscreenPageLimit(reposAdapter.getCount());
            viewPager.setAdapter(reposAdapter);

            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private class ReposPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public ReposPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList != null ? fragmentList : new ArrayList<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (fragmentList.get(position) instanceof TitleProvider) {
                return getResources().getString(((TitleProvider) fragmentList.get(position)).getTitle());
            } else {
                return "";
            }
        }
    }
}
