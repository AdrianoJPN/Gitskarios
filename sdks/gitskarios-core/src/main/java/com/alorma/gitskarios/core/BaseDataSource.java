package com.alorma.gitskarios.core;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by a557114 on 08/09/2015.
 */
public abstract class BaseDataSource<Z>  {

    private Callback<Z> callback;

    public BaseDataSource() {

    }

    public abstract void executeAsync(Callback<Z> callback);

    public interface Callback<Z> {
        void onResponse(Z z, Response response);

        void onFail(RetrofitError error);
    }

}
