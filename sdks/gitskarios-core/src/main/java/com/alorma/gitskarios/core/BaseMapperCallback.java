package com.alorma.gitskarios.core;

import com.alorma.gitskarios.core.client.BaseClient;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by a557114 on 09/09/2015.
 */
public abstract class BaseMapperCallback<ApiDto, K> implements BaseClient.OnResultCallback<ApiDto> {

    private BaseDataSource.Callback<K>  callback;

    public BaseMapperCallback(BaseDataSource.Callback<K> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponseOk(ApiDto apiDto, Response r) {
        if (callback != null) {
            callback.onResponse(getMapper().toCore(apiDto), r);
        }
    }

    protected abstract BaseMapper<ApiDto, K> getMapper();

    @Override
    public void onFail(RetrofitError error) {
        if (callback != null) {
            callback.onFail(error);
        }
    }
}
