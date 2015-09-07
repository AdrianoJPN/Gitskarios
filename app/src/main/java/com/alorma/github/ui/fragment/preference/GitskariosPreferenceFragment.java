package com.alorma.github.ui.fragment.preference;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.alorma.github.Interceptor;
import com.alorma.github.R;
import com.alorma.github.UrlsManager;
import com.alorma.github.basesdk.client.StoreCredentials;
import com.alorma.github.sdk.utils.GitskariosSettings;
import com.alorma.github.ui.fragment.ChangelogDialog;

public class GitskariosPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String PREF_INTERCEPT = "pref_intercept";
    public static final String REPOS_SORT = "repos_sort";
    public static final String REPOS_FILE_TYPE = "repos_download_type";
    public static final String REAUTHORIZE = "reauthorize";
    public static final String GITSKARIOS = "gitskarios";
    public static final String CHANGELOG = "changelog";

    private StoreCredentials credentials;
    private ChangelogDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.main_prefs);

        CheckBoxPreference intercetor = (CheckBoxPreference) findPreference(PREF_INTERCEPT);

        ComponentName componentName = new ComponentName(getActivity(), Interceptor.class);
        int componentEnabledSetting = getActivity().getPackageManager().getComponentEnabledSetting(componentName);

        GitskariosSettings settings = new GitskariosSettings(getActivity());
        boolean interceptState = settings.interceptState(componentEnabledSetting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED);

        intercetor.setChecked(interceptState);

        intercetor.setOnPreferenceChangeListener(this);

        findPreference(REPOS_SORT).setOnPreferenceChangeListener(this);

        findPreference(REPOS_FILE_TYPE).setOnPreferenceChangeListener(this);

        Preference gitskarios = findPreference(GITSKARIOS);
        gitskarios.setOnPreferenceClickListener(this);

        Preference changelog = findPreference(CHANGELOG);
        changelog.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(GITSKARIOS)) {
            startActivity(new UrlsManager(getActivity()).manageRepos(Uri.parse("https://github.com/gitskarios/Gitskarios")));
        } else if (preference.getKey().equals(CHANGELOG)) {
            dialog = ChangelogDialog.create();
            dialog.show(getFragmentManager(), "changelog");
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        GitskariosSettings settings = new GitskariosSettings(getActivity());
        if (preference.getKey().equals(PREF_INTERCEPT)) {
            Boolean value = (Boolean) newValue;

            settings.saveInterceptState(value);

            int flag = value ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

            ComponentName componentName = new ComponentName(getActivity(), Interceptor.class);
            getActivity().getPackageManager().setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP);

        } else if (preference.getKey().equals(REPOS_SORT)) {
            settings.saveRepoSort(String.valueOf(newValue));
        } else if (preference.getKey().equals(REPOS_FILE_TYPE)) {
            settings.saveDownloadFileType(String.valueOf(newValue));
        }
        return true;
    }


    @Override
    public void onStop() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}