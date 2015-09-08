package com.alorma.github.ui.callbacks;

import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.client.BaseClient;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by a557114 on 08/09/2015.
 */
@Deprecated
public abstract class TransitionalCallback<Api, Core> implements BaseClient.OnResultCallback<Api>{

    private BaseClient.OnResultCallback<Core> callback;

    public TransitionalCallback(BaseClient.OnResultCallback<Core> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponseOk(Api api, Response r) {
        if (callback != null) {
            callback.onResponseOk(geMapper().toCore(api), r);
        }
    }

    protected abstract BaseMapper<Api, Core> geMapper();

    @Override
    public void onFail(RetrofitError error) {
        if (callback != null) {
            callback.onFail(error);
        }
    }
}
