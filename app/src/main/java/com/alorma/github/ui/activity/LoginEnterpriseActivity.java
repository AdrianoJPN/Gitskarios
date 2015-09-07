package com.alorma.github.ui.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alorma.github.R;
import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;

import java.util.ArrayList;
import java.util.List;

public class LoginEnterpriseActivity extends AccountAuthenticatorActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_login_enterprise);

    }
}
