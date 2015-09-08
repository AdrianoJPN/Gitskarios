package com.alorma.gitskarios.core.client;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.alorma.gitskarios.core.ApiConnection;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Response;
import retrofit.converter.Converter;

public abstract class BaseClient<ApiDto> implements Callback<ApiDto>, RequestInterceptor, RestAdapter.Log {

    private StoreCredentials storeCredentials;

    protected final Context context;
    private OnResultCallback<ApiDto> onResultCallback;
    protected Handler handler;
    private ApiConnection client;

    public Uri last;
    public Uri next;
    public int lastPage;
    public int nextPage;

    public BaseClient(Context context, ApiConnection client) {
        this.client = client;
        this.context = context.getApplicationContext();
        storeCredentials = new StoreCredentials(context);
    }

    private RestAdapter getRestAdapter() {
        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder()
                .setEndpoint(client.getApiEndpoint())
                .setRequestInterceptor(this)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(this);

        if (customConverter() != null) {
            restAdapterBuilder.setConverter(customConverter());
        }

        if (getInterceptor() != null) {
            restAdapterBuilder.setClient(getInterceptor());
        }

        return restAdapterBuilder.build();
    }

    @Nullable
    protected Client getInterceptor() {
        return null;
    }

    public void execute() {
        try {
            handler = new Handler();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getToken() != null) {
            executeService(getRestAdapter());
        }
    }

    public ApiDto executeSync() {
        if (getToken() != null) {
            return executeServiceSync(getRestAdapter());
        }
        return null;
    }

    protected Converter customConverter() {
        return null;
    }

    protected abstract void executeService(RestAdapter restAdapter);

    protected abstract ApiDto executeServiceSync(RestAdapter restAdapter);

    @Override
    public void success(final ApiDto apiDto, final Response response) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sendResponse(apiDto, response);
                }
            });
        } else {
            sendResponse(apiDto, response);
        }
    }


    private void sendResponse(ApiDto coreDto, Response response) {
        if (onResultCallback != null) {
            onResultCallback.onResponseOk(coreDto, response);
        }
    }

    @Override
    public void failure(final RetrofitError error) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sendError(error);
                }
            });
        } else {
            sendError(error);
        }
    }

    private void sendError(RetrofitError error) {
        if (error.getResponse() != null && error.getResponse().getStatus() == 401) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
            manager.sendBroadcast(new UnAuthIntent(storeCredentials.token()));
        } else {
            if (onResultCallback != null) {
                onResultCallback.onFail(error);
            }
        }
    }

    public OnResultCallback<ApiDto> getOnResultCallback() {
        return onResultCallback;
    }

    public void setOnResultCallback(OnResultCallback<ApiDto> onResultCallback) {
        this.onResultCallback = onResultCallback;
    }


    protected String getToken() {
        return storeCredentials.token();
    }

    public Context getContext() {
        return context;
    }

    public interface OnResultCallback<ApiDto> {
        void onResponseOk(ApiDto apiDto, Response r);

        void onFail(RetrofitError error);
    }

    public ApiConnection getClient() {
        return client;
    }

    public void setStoreCredentials(StoreCredentials storeCredentials) {
        this.storeCredentials = storeCredentials;
    }
}
