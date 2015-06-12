package com.alorma.github.ui.fragment.detail.repo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.Branch;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.ui.activity.IssuesListActivity;
import com.alorma.gitskarios.basesdk.client.BaseClient;
import com.alorma.github.sdk.services.repo.GetReadmeContentsClient;
import com.alorma.github.ui.ErrorHandler;
import com.alorma.github.ui.fragment.base.BaseFragment;
import com.alorma.github.ui.listeners.TitleProvider;
import com.alorma.github.utils.AttributesUtils;
import com.gh4a.utils.UiUtils;
import com.github.mobile.util.HtmlUtils;
import com.github.mobile.util.HttpImageGetter;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bernat on 22/07/2014.
 */
public class ReadmeFragment extends BaseFragment implements BaseClient.OnResultCallback<String>, BranchManager, TitleProvider, View.OnClickListener {

    private static final String REPO_INFO = "REPO_INFO";
    private static final String OPEN_ISSUES_COUNT = "OPEN_ISSUES_COUNT";
    private RepoInfo repoInfo;

    private TextView htmlContentView;
    private TextView issuesNumber;

    private UpdateReceiver updateReceiver;
    private SmoothProgressBar progressBar;
    private int openIssuesCount = -1;

    public static ReadmeFragment newInstance(RepoInfo repoInfo, int open_issues_count) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(REPO_INFO, repoInfo);
        bundle.putInt(OPEN_ISSUES_COUNT, open_issues_count);

        ReadmeFragment f = new ReadmeFragment();
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.readme_fragment, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            loadArguments();

            progressBar = (SmoothProgressBar) view.findViewById(R.id.progress);
            htmlContentView = (TextView) view.findViewById(R.id.htmlContentView);
            issuesNumber = (TextView) view.findViewById(R.id.issuesNumber);

            int color = AttributesUtils.getPrimaryColor(getActivity());

            progressBar.setSmoothProgressDrawableColor(color);

            getContent();

            if (openIssuesCount >= 0) {
                issuesNumber.setText(String.valueOf(openIssuesCount));
            }

            issuesNumber.setOnClickListener(this);
        }
    }

    protected void loadArguments() {
        if (getArguments() != null) {
            repoInfo = getArguments().getParcelable(REPO_INFO);
            openIssuesCount = getArguments().getInt(OPEN_ISSUES_COUNT);
        }
    }

    private void getContent() {

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.progressiveStart();
        }

        GetReadmeContentsClient repoMarkdownClient = new GetReadmeContentsClient(getActivity(), repoInfo);
        repoMarkdownClient.setCallback(this);
        repoMarkdownClient.execute();
    }

    @Override
    public void onResponseOk(final String htmlContent, Response r) {

        if (progressBar != null) {
            progressBar.progressiveStop();
            progressBar.setVisibility(View.INVISIBLE);
        }

        if (htmlContent != null) {
            String htmlCode = HtmlUtils.format(htmlContent).toString();
            HttpImageGetter imageGetter = new HttpImageGetter(getActivity());

            imageGetter.repoInfo(repoInfo);
            imageGetter.bind(htmlContentView, htmlCode, repoInfo.hashCode());

            htmlContentView.setMovementMethod(UiUtils.CHECKING_LINK_METHOD);
        }
    }

    @Override
    public void onFail(RetrofitError error) {
        if (progressBar != null) {
            progressBar.progressiveStop();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setCurrentBranch(Branch branch) {
        if (getActivity() != null) {
            GetReadmeContentsClient repoMarkdownClient = new GetReadmeContentsClient(getActivity(), repoInfo);
            repoMarkdownClient.setCallback(this);
            repoMarkdownClient.execute();
        }
    }

    private void onError(String tag, RetrofitError error) {
        ErrorHandler.onError(getActivity(), "MarkdownFragment: " + tag, error);
    }

    @Override
    public int getTitle() {
        return R.string.markdown_fragment_title;
    }

    public void reload() {
        getContent();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateReceiver = new UpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(updateReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(updateReceiver);
    }

    public void setIssuesNumber(int openIssues) {
        this.openIssuesCount = openIssues;
        if (issuesNumber != null) {
            issuesNumber.setText(String.valueOf(openIssuesCount));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.issuesNumber:
                Intent intent = IssuesListActivity.createLauncher(getActivity(), repoInfo);
                startActivity(intent);
                break;
        }
    }

    public class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (isOnline(context)) {
                reload();
            }
        }

        public boolean isOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo netInfoWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) || (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting());
        }
    }

}
