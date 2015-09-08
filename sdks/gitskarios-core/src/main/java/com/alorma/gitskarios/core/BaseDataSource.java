package com.alorma.gitskarios.core;

import com.alorma.gitskarios.core.client.BaseClient;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by a557114 on 08/09/2015.
 */
public abstract class BaseDataSource<T extends BaseClient<K>, K, Z> implements BaseClient.OnResultCallback<K> {

    private Callback<Z> callback;

    public BaseDataSource() {

    }

    public abstract T getApiClient();

    public abstract BaseMapper<K, Z> getMapper();

    public void executeAsync(Callback<Z> callback) {
        this.callback = callback;
        T apiClient = getApiClient();
        if (apiClient != null) {
            apiClient.setOnResultCallback(this);
            apiClient.execute();
        }
    }

    @Override
    public void onResponseOk(K k, Response r) {
        if (callback != null) {
            callback.onResponse(getMapper().toCore(k), r);
        }
    }

    @Override
    public void onFail(RetrofitError error) {
        if (callback != null) {
            callback.onFail(error);
        }
    }

    public interface Callback<Z> {
        void onResponse(Z z, Response response);

        void onFail(RetrofitError error);
    }

}
