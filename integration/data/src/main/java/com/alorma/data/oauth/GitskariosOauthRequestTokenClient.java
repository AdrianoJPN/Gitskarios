package com.alorma.data.oauth;

import android.content.Context;
import android.net.Uri;

import com.alorma.github.sdk.security.GitHub;
import com.alorma.githubintegration.oauth.GithubOauthRequestTokenDataSource;
import com.alorma.gitskarios.core.ApiConnection;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.GitskariosFactory;
import com.alorma.gitskarios.core.Token;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GitskariosOauthRequestTokenClient
        implements GitskariosFactory<Token> {

    private ApiConnection connection;
    private Context context;
    private String code;

    public GitskariosOauthRequestTokenClient(Context context, String code, ApiConnection connection) {
        this.connection = connection;
        this.context = context;
        this.code = code;
    }

    @Override
    public BaseDataSource<Token> create() {
        if (connection.getType().equals(new GitHub().getType())) {
            return new GithubOauthRequestTokenDataSource(context, code);
        }
        return null;
    }

    @Override
    public GitskariosFactory<Token> setApiConnection(ApiConnection apiConnection) {
        return null;
    }

}
