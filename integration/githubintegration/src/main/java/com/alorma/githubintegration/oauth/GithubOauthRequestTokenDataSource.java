package com.alorma.githubintegration.oauth;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alorma.github.sdk.services.login.RequestTokenClient;
import com.alorma.gitskarios.core.BaseDataSource;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.BaseMapperCallback;
import com.alorma.gitskarios.core.Token;

/**
 * Created by a557114 on 09/09/2015.
 */
public class GithubOauthRequestTokenDataSource extends BaseDataSource<Token> {
    private Context context;
    private String code;

    public GithubOauthRequestTokenDataSource(Context context, String code) {
        this.context = context;
        this.code = code;
    }


    @Override
    public void executeAsync(Callback<Token> callback) {
        RequestTokenClient requestTokenClient = new RequestTokenClient(context, code);
        requestTokenClient.setOnResultCallback(new BaseMapperCallback<Token, Token>(callback) {
            @Override
            protected BaseMapper<Token, Token> getMapper() {
                return new BaseMapper<Token, Token>() {
                    @NonNull
                    @Override
                    public Token toCore(@NonNull Token token) {
                        return token;
                    }
                };
            }
        });
        requestTokenClient.execute();;
    }
}
