package com.alorma.github.ui.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;

import com.alorma.github.R;

public class LoginEnterpriseActivity extends AccountAuthenticatorActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_login_enterprise);
    }
}
