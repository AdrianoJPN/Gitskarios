package com.alorma.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alorma.github.R;
import com.alorma.github.cache.QnCacheProvider;
import com.alorma.github.ui.actions.ActionCallback;
import com.alorma.github.ui.actions.ChangeAssigneeAction;
import com.alorma.github.ui.actions.CloseAction;
import com.alorma.github.ui.actions.ReopenAction;
import com.alorma.github.ui.actions.ShareAction;
import com.alorma.gitskarios.core.client.BaseClient;
import com.alorma.gitskarios.core.client.StoreCredentials;
import com.alorma.github.sdk.bean.dto.request.CreateMilestoneRequestDTO;
import com.alorma.github.sdk.bean.dto.request.EditIssueBodyRequestDTO;
import com.alorma.github.sdk.bean.dto.request.EditIssueLabelsRequestDTO;
import com.alorma.github.sdk.bean.dto.request.EditIssueMilestoneRequestDTO;
import com.alorma.github.sdk.bean.dto.request.EditIssueRequestDTO;
import com.alorma.github.sdk.bean.dto.request.EditIssueTitleRequestDTO;
import com.alorma.github.sdk.bean.dto.response.GithubComment;
import com.alorma.github.sdk.bean.dto.response.Issue;
import com.alorma.github.sdk.bean.dto.response.IssueState;
import com.alorma.github.sdk.bean.dto.response.Label;
import com.alorma.github.sdk.bean.dto.response.Milestone;
import com.alorma.github.sdk.bean.dto.response.MilestoneState;
import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.info.IssueInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.bean.issue.IssueStory;
import com.alorma.github.sdk.services.issues.ChangeIssueStateClient;
import com.alorma.github.sdk.services.issues.CreateMilestoneClient;
import com.alorma.github.sdk.services.issues.EditIssueClient;
import com.alorma.github.sdk.services.issues.GetMilestonesClient;
import com.alorma.github.sdk.services.issues.GithubIssueLabelsClient;
import com.alorma.github.sdk.services.issues.NewIssueCommentClient;
import com.alorma.github.sdk.services.issues.story.IssueStoryLoader;
import com.alorma.github.sdk.services.repo.GetRepoClient;
import com.alorma.github.sdk.utils.GitskariosSettings;
import com.alorma.github.ui.ErrorHandler;
import com.alorma.github.ui.activity.base.BackActivity;
import com.alorma.github.ui.adapter.issues.IssueDetailAdapter;
import com.alorma.github.ui.listeners.IssueDetailRequestListener;
import com.alorma.github.utils.ShortcutUtils;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class IssueDetailActivity extends BackActivity implements BaseClient.OnResultCallback<IssueStory>, View.OnClickListener, IssueDetailRequestListener {

    public static final String ISSUE_INFO = "ISSUE_INFO";
    public static final String ISSUE_INFO_REPO_NAME = "ISSUE_INFO_REPO_NAME";
    public static final String ISSUE_INFO_REPO_OWNER = "ISSUE_INFO_REPO_OWNER";
    public static final String ISSUE_INFO_NUMBER = "ISSUE_INFO_NUMBER";

    private static final int NEW_COMMENT_REQUEST = 1243;
    private static final int ISSUE_BODY_EDIT = 4252;

    private boolean shouldRefreshOnBack;
    private IssueInfo issueInfo;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private IssueStory issueStory;
    private int primary;
    private int primaryDark;
    private ProgressBar loadingView;
    public Repo repository;

    public static Intent createLauncherIntent(Context context, IssueInfo issueInfo) {
        Bundle bundle = new Bundle();

        bundle.putParcelable(ISSUE_INFO, issueInfo);

        Intent intent = new Intent(context, IssueDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createShortcutLauncherIntent(Context context, IssueInfo issueInfo) {
        Bundle bundle = new Bundle();

        bundle.putString(ISSUE_INFO_REPO_NAME, issueInfo.repoInfo.name);
        bundle.putString(ISSUE_INFO_REPO_OWNER, issueInfo.repoInfo.owner);
        bundle.putInt(ISSUE_INFO_NUMBER, issueInfo.num);

        Intent intent = new Intent(context, IssueDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);

        if (getIntent().getExtras() != null) {

            issueInfo = getIntent().getExtras().getParcelable(ISSUE_INFO);

            if (issueInfo == null && getIntent().getExtras().containsKey(ISSUE_INFO_NUMBER)) {
                String name = getIntent().getExtras().getString(ISSUE_INFO_REPO_NAME);
                String owner = getIntent().getExtras().getString(ISSUE_INFO_REPO_OWNER);

                RepoInfo repoInfo = new RepoInfo();
                repoInfo.name = name;
                repoInfo.owner = owner;

                int num = getIntent().getExtras().getInt(ISSUE_INFO_NUMBER);

                issueInfo = new IssueInfo();
                issueInfo.repoInfo = repoInfo;
                issueInfo.num = num;
            }

            primary = getResources().getColor(R.color.primary);
            primaryDark = getResources().getColor(R.color.primary_dark_alpha);

            findViews();
        }
    }

    private void checkEditTitle() {
        if (issueInfo != null && issueStory != null && issueStory.issue != null) {

            StoreCredentials credentials = new StoreCredentials(this);

            GitskariosSettings settings = new GitskariosSettings(this);
            if (settings.shouldShowDialogEditIssue()) {
                if (issueInfo.repoInfo.permissions != null && issueInfo.repoInfo.permissions.push) {
                    showEditDialog(R.string.dialog_edit_issue_edit_title_and_body_by_owner);
                } else if (issueStory.issue.user.login.equals(credentials.getUserName())) {
                    showEditDialog(R.string.dialog_edit_issue_edit_title_and_body_by_author);
                }
            }
        }
    }

    private void showEditDialog(int content) {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_edit_issue)
                .content(content)
                .positiveText(R.string.ok)
                .show();
    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = (FloatingActionButton) findViewById(R.id.fabButton);
        loadingView = (ProgressBar) findViewById(R.id.loading_view);

        IconicsDrawable drawable = new IconicsDrawable(this, Octicons.Icon.oct_comment_discussion).color(Color.WHITE).sizeDp(24);

        fab.setImageDrawable(drawable);

        ViewCompat.setElevation(getToolbar(), getResources().getDimensionPixelOffset(R.dimen.gapSmall));
    }

    @Override
    protected void getContent() {
        super.getContent();
        loadingView.setVisibility(View.VISIBLE);
        boolean contains = QnCacheProvider.getInstance(QnCacheProvider.TYPE.ISSUE).contains(issueInfo.toString());
        if (checkPermissions(issueInfo)) {
            GetRepoClient repoClient = new GetRepoClient(this, issueInfo.repoInfo);
            repoClient.setOnResultCallback(new BaseClient.OnResultCallback<Repo>() {

                @Override
                public void onResponseOk(Repo repo, Response r) {
                    issueInfo.repoInfo.permissions = repo.permissions;
                    repository = repo;
                    getContent();
                }

                @Override
                public void onFail(RetrofitError error) {

                }
            });
            repoClient.execute();
        } else if (contains) {
            onResponseOk(QnCacheProvider.getInstance(QnCacheProvider.TYPE.ISSUE).<IssueStory>get(issueInfo.toString()), null);
            loadIssue();
        } else {
            loadIssue();
        }
    }

    private void loadIssue() {
        IssueStoryLoader issueStoryLoader = new IssueStoryLoader(this, issueInfo);
        issueStoryLoader.setOnResultCallback(this);
        issueStoryLoader.execute();
    }

    private boolean checkPermissions(IssueInfo issueInfo) {
        return issueInfo != null
                && issueInfo.repoInfo != null
                && issueInfo.repoInfo.permissions == null;
    }

    @Override
    public void onResponseOk(IssueStory issueStory, Response r) {
        hideProgressDialog();
        this.issueStory = issueStory;
        this.issueStory.issue.repository = repository;

        checkEditTitle();
        applyIssue();
        QnCacheProvider.getInstance(QnCacheProvider.TYPE.ISSUE).set(issueInfo.toString(), issueStory);
    }

    private void applyIssue() {
        loadingView.setVisibility(View.GONE);
        changeColor(issueStory.issue);

        fab.setVisibility(issueStory.issue.locked ? View.GONE : View.VISIBLE);
        fab.setOnClickListener(issueStory.issue.locked ? null : this);

        String status = getString(R.string.issue_status_open);
        if (IssueState.closed == issueStory.issue.state) {
            status = getString(R.string.issue_status_close);
        }
        setTitle("#" + issueStory.issue.number + " " + status);
        IssueDetailAdapter adapter = new IssueDetailAdapter(this, getLayoutInflater(), issueStory, issueInfo.repoInfo);
        adapter.setIssueDetailRequestListener(this);
        recyclerView.setAdapter(adapter);

        invalidateOptionsMenu();
    }

    private void changeColor(Issue issue) {
        int colorState = getResources().getColor(R.color.issue_state_close);
        int colorStateDark = getResources().getColor(R.color.issue_state_close_dark);
        if (IssueState.open == issue.state) {
            colorState = getResources().getColor(R.color.issue_state_open);
            colorStateDark = getResources().getColor(R.color.issue_state_open_dark);
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), primary, colorState);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (Integer) animator.getAnimatedValue();
                if (getToolbar() != null) {
                    getToolbar().setBackgroundColor(color);
                }
            }

        });

        ValueAnimator colorAnimationStatus = ValueAnimator.ofObject(new ArgbEvaluator(), primaryDark, colorStateDark);
        colorAnimationStatus.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (Integer) animator.getAnimatedValue();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(color);
                }
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(750);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(colorAnimation, colorAnimationStatus);
        final int finalColorState = colorState;
        final int finalColorStateDark = colorStateDark;
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                primary = finalColorState;
                primaryDark = finalColorStateDark;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Override
    public void onFail(RetrofitError error) {
        hideProgressDialog();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(R.string.ups);
        if (error != null && error.getResponse() != null && error.getResponse().getReason() != null) {
            builder.content(getString(R.string.issue_detail_error, issueInfo.toString(), error.getResponse().getReason()));
        }
        builder.positiveText(R.string.retry);
        builder.negativeText(R.string.accept);
        builder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                getContent();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == fab.getId()) {
            if (!issueStory.issue.locked) {
                onAddComment();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.issueStory != null) {
            if (issueInfo.repoInfo.permissions != null && issueInfo.repoInfo.permissions.push) {
                getMenuInflater().inflate(R.menu.issue_detail, menu);
            } else {
                getMenuInflater().inflate(R.menu.issue_detail_no_permissions, menu);
            }

            MenuItem item = menu.findItem(R.id.share_issue);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_menu_share_mtrl_alpha, getTheme()));
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_menu_share_mtrl_alpha));
            }

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.issueStory != null) {

            if (issueInfo.repoInfo.permissions != null && issueInfo.repoInfo.permissions.push) {
                if (menu.findItem(R.id.action_close_issue) != null) {
                    menu.removeItem(R.id.action_close_issue);
                }
                if (menu.findItem(R.id.action_reopen_issue) != null) {
                    menu.removeItem(R.id.action_reopen_issue);
                }
                if (issueStory.issue.state == IssueState.closed) {
                    MenuItem menuItem = menu.add(0, R.id.action_reopen_issue, 1, getString(R.string.reopenIssue));
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                } else {

                    MenuItem menuItem = menu.add(0, R.id.action_close_issue, 1, getString(R.string.closeIssue));
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                }
            }
        }

        return true;
    }

    public void onAddComment() {
        String hint = getString(R.string.add_comment);
        Intent intent = ContentEditorActivity.createLauncherIntent(this, issueInfo.repoInfo, issueInfo.num, hint, null, false, false);
        startActivityForResult(intent, NEW_COMMENT_REQUEST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(shouldRefreshOnBack ? RESULT_FIRST_USER : RESULT_OK);
                finish();
                break;
            case R.id.action_close_issue:
                new CloseAction(this, issueInfo, R.string.closeIssue).setCallback(new ActionCallback<Issue>() {
                    @Override
                    public void onResult(Issue issue) {
                        getContent();
                        Snackbar.make(fab, R.string.issue_change, Snackbar.LENGTH_SHORT).show();
                    }
                }).execute();
                break;
            case R.id.action_reopen_issue:
                new ReopenAction(this, issueInfo, R.string.reopenIssue).setCallback(new ActionCallback<Issue>() {
                    @Override
                    public void onResult(Issue issue) {
                        getContent();
                        Snackbar.make(fab, R.string.issue_change, Snackbar.LENGTH_SHORT).show();
                    }
                }).execute();
                break;
            case R.id.issue_edit_milestone:
                editMilestone();
                break;
            case R.id.issue_edit_assignee:
                new ChangeAssigneeAction(this, issueInfo).setCallback(new AssigneActionCallback()).execute();
                break;
            case R.id.issue_edit_labels:
                openLabels();
                break;
            case R.id.share_issue:
                if (issueStory != null && issueStory.issue != null) {

                    String title = issueInfo.toString();
                    String url = issueStory.issue.html_url;

                    new ShareAction(this, title, url).execute();
                }
                break;
            case R.id.action_add_shortcut:
                ShortcutUtils.addShortcut(this, issueInfo);
                break;
        }

        return true;
    }

    private void editMilestone() {
        GetMilestonesClient milestonesClient = new GetMilestonesClient(this, issueInfo.repoInfo, MilestoneState.open);
        milestonesClient.setOnResultCallback(new MilestonesCallback());
        milestonesClient.execute();

        showProgressDialog(R.style.SpotDialog_loading_milestones);
    }

    @Override
    public void onTitleEditRequest() {
        new MaterialDialog.Builder(this)
                .title(R.string.edit_issue_title)
                .input(null, issueStory.issue.title, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                        EditIssueTitleRequestDTO editIssueTitleRequestDTO = new EditIssueTitleRequestDTO();
                        editIssueTitleRequestDTO.title = charSequence.toString();
                        executeEditIssue(editIssueTitleRequestDTO, R.string.issue_change_title);
                    }
                })
                .positiveText(R.string.edit_issue_button_ok)
                .neutralText(R.string.edit_issue_button_neutral)
                .show();
    }

    @Override
    public void onContentEditRequest() {
        String body = issueStory.issue.body != null ? issueStory.issue.body.replace("\n", "<br />") : "";
        Intent launcherIntent = ContentEditorActivity.createLauncherIntent(this, issueInfo.repoInfo, issueInfo.num, getString(R.string.edit_issue_body_hint), body, true, false);
        startActivityForResult(launcherIntent, ISSUE_BODY_EDIT);
    }

    private class MilestonesCallback implements BaseClient.OnResultCallback<List<Milestone>> {
        @Override
        public void onResponseOk(final List<Milestone> milestones, Response r) {
            hideProgressDialog();
            if (milestones.size() == 0) {
                showCreateMilestone();
            } else {
                String[] itemsMilestones = new String[milestones.size()];

                for (int i = 0; i < milestones.size(); i++) {
                    itemsMilestones[i] = milestones.get(i).title;
                }

                int selectedMilestone = -1;
                for (int i = 0; i < milestones.size(); i++) {
                    if (IssueDetailActivity.this.issueStory.issue.milestone != null) {
                        String currentMilestone = IssueDetailActivity.this.issueStory.issue.milestone.title;
                        if (currentMilestone != null && currentMilestone.equals(milestones.get(i).title)) {
                            selectedMilestone = i;
                            break;
                        }
                    }
                }

                MaterialDialog.Builder builder = new MaterialDialog.Builder(IssueDetailActivity.this)
                        .title(R.string.select_milestone)
                        .items(itemsMilestones)
                        .itemsCallbackSingleChoice(selectedMilestone, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence text) {

                                addMilestone(milestones.get(i));

                                return false;
                            }
                        })
                        .forceStacking(true)
                        .widgetColorRes(R.color.primary)
                        .negativeText(R.string.add_milestone);

                if (selectedMilestone != -1) {
                    builder.neutralText(R.string.clear_milestone);
                }

                builder.callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        showCreateMilestone();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        clearMilestone();
                    }
                });

                builder.show();
            }
        }

        @Override
        public void onFail(RetrofitError error) {

        }
    }

    private void showCreateMilestone() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(R.string.add_milestone);
        builder.content(R.string.add_milestone_description);
        builder.input(R.string.add_milestone_hint, 0, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(MaterialDialog materialDialog, CharSequence milestoneName) {
                createMilestone(milestoneName.toString());
            }
        })
                .negativeText(R.string.cancel);

        builder.show();
    }

    private void createMilestone(String milestoneName) {
        CreateMilestoneRequestDTO createMilestoneRequestDTO = new CreateMilestoneRequestDTO(milestoneName);

        CreateMilestoneClient createMilestoneClient = new CreateMilestoneClient(this, issueInfo.repoInfo, createMilestoneRequestDTO);
        createMilestoneClient.setOnResultCallback(new BaseClient.OnResultCallback<Milestone>() {
            @Override
            public void onResponseOk(Milestone milestone, Response r) {
                addMilestone(milestone);
            }

            @Override
            public void onFail(RetrofitError error) {
                hideProgressDialog();
            }
        });
        createMilestoneClient.execute();
    }

    private void addMilestone(Milestone milestone) {
        showProgressDialog(R.style.SpotDialog_loading_adding_milestones);
        EditIssueMilestoneRequestDTO editIssueRequestDTO = new EditIssueMilestoneRequestDTO();
        editIssueRequestDTO.milestone = milestone.number;
        executeEditIssue(editIssueRequestDTO, R.string.issue_change_add_milestone);
    }

    private void clearMilestone() {
        showProgressDialog(R.style.SpotDialog_clear_milestones);
        EditIssueMilestoneRequestDTO editIssueRequestDTO = new EditIssueMilestoneRequestDTO();
        editIssueRequestDTO.milestone = null;
        executeEditIssue(editIssueRequestDTO, R.string.issue_change_clear_milestone);
    }

    private void executeEditIssue(final EditIssueRequestDTO editIssueRequestDTO, final int changedText) {
        EditIssueClient client = new EditIssueClient(IssueDetailActivity.this, issueInfo, editIssueRequestDTO);
        client.setOnResultCallback(new BaseClient.OnResultCallback<Issue>() {
            @Override
            public void onResponseOk(Issue issue, Response r) {
                shouldRefreshOnBack = true;
                hideProgressDialog();
                getContent();

                Snackbar.make(fab, changedText, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(RetrofitError error) {
                ErrorHandler.onError(IssueDetailActivity.this, "Issue detail", error);
                hideProgressDialog();
            }
        });
        client.execute();
    }

    /**
     * Labels
     */

    private void openLabels() {
        GithubIssueLabelsClient labelsClient = new GithubIssueLabelsClient(this, issueInfo.repoInfo);
        labelsClient.setOnResultCallback(new LabelsCallback());
        labelsClient.execute();
    }

    private class LabelsCallback implements BaseClient.OnResultCallback<List<Label>> {

        private CharSequence[] selectedLabels;
        private Integer[] positionsSelectedLabels;

        @Override
        public void onResponseOk(List<Label> labels, Response r) {
            if (labels != null) {
                List<String> items = new ArrayList<>();
                List<String> selectedLabels = new ArrayList<>();
                List<Integer> positionsSelectedLabels = new ArrayList<>();

                List<String> currentLabels = new ArrayList<>();
                for (Label label : issueStory.issue.labels) {
                    currentLabels.add(label.name);
                }

                int i = 0;
                for (Label label : labels) {
                    items.add(label.name);
                    if (currentLabels.contains(label.name)) {
                        selectedLabels.add(label.name);
                        positionsSelectedLabels.add(i);
                    }
                    i++;
                }

                LabelsCallback.this.selectedLabels = selectedLabels.toArray(new String[selectedLabels.size()]);

                MaterialDialog.Builder builder = new MaterialDialog.Builder(IssueDetailActivity.this);
                builder.items(items.toArray(new String[items.size()]));
                builder.alwaysCallMultiChoiceCallback();
                builder.itemsCallbackMultiChoice(positionsSelectedLabels.toArray(new Integer[positionsSelectedLabels.size()]), new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, Integer[] integers, CharSequence[] charSequences) {
                        LabelsCallback.this.selectedLabels = charSequences;
                        LabelsCallback.this.positionsSelectedLabels = integers;
                        return true;
                    }
                });
                builder.forceStacking(true);
                builder.positiveText(R.string.ok);
//                builder.neutralText(R.string.add_new_label);
                builder.negativeText(R.string.clear_labels);
                builder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        setLabels(LabelsCallback.this.selectedLabels);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        LabelsCallback.this.selectedLabels = null;
                        LabelsCallback.this.positionsSelectedLabels = null;
                        setLabels(null);
                    }

//                    @Override
//                    public void onNeutral(MaterialDialog dialog) {
//                        super.onNeutral(dialog);
//                    }
                });
                builder.show();
            }
        }

        @Override
        public void onFail(RetrofitError error) {
            ErrorHandler.onError(IssueDetailActivity.this, "Issue detail", error);
        }
    }

    private void setLabels(CharSequence[] selectedLabels) {
        if (selectedLabels != null) {

            if (selectedLabels.length > 0) {
                String[] labels = new String[selectedLabels.length];
                for (int i = 0; i < selectedLabels.length; i++) {
                    labels[i] = selectedLabels[i].toString();
                }
                EditIssueLabelsRequestDTO labelsRequestDTO = new EditIssueLabelsRequestDTO();
                labelsRequestDTO.labels = labels;
                executeEditIssue(labelsRequestDTO, R.string.issue_change_labels);
            } else {
                EditIssueLabelsRequestDTO labelsRequestDTO = new EditIssueLabelsRequestDTO();
                labelsRequestDTO.labels = new String[]{};
                executeEditIssue(labelsRequestDTO, R.string.issue_change_labels_clear);
            }
        } else {
            EditIssueLabelsRequestDTO labelsRequestDTO = new EditIssueLabelsRequestDTO();
            labelsRequestDTO.labels = new String[]{};
            executeEditIssue(labelsRequestDTO, R.string.issue_change_labels_clear);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(shouldRefreshOnBack ? RESULT_FIRST_USER : RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == NEW_COMMENT_REQUEST) {
                showProgressDialog(R.style.SpotDialog_loading_adding_comment);
                String body = data.getStringExtra(ContentEditorActivity.CONTENT);
                final NewIssueCommentClient client = new NewIssueCommentClient(this, issueInfo, body);
                client.setOnResultCallback(new BaseClient.OnResultCallback<GithubComment>() {
                    @Override
                    public void onResponseOk(GithubComment githubComment, Response r) {
                        getContent();
                        Snackbar.make(fab, R.string.add_comment_issue_ok, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(RetrofitError error) {
                        // TODO on comment fail

                        Snackbar.make(fab, R.string.add_comment_issue_fail, Snackbar.LENGTH_SHORT).setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                client.execute();
                            }
                        }).show();
                    }
                });
                client.execute();

            } else if (requestCode == ISSUE_BODY_EDIT) {
                EditIssueBodyRequestDTO bodyRequestDTO = new EditIssueBodyRequestDTO();
                bodyRequestDTO.body = data.getStringExtra(ContentEditorActivity.CONTENT);

                executeEditIssue(bodyRequestDTO, R.string.issue_change_body);
            }
        }
    }

    private class AssigneActionCallback implements ActionCallback<Boolean> {
        @Override
        public void onResult(Boolean result) {
            if (result) {
                Snackbar.make(fab, "Assignee changed", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(fab, "Assignee change failed", Snackbar.LENGTH_SHORT).show();
            }
            getContent();
        }
    }
}
