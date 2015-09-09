package com.alorma.gitlabsdk.security.client;

import android.content.Context;
import android.util.Log;

import com.alorma.gitlabsdk.security.Gitlab;
import com.alorma.gitskarios.core.client.BaseClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class GitlabClient<K> extends BaseClient<K> {

    public GitlabClient(Context context) {
        super(context, new Gitlab());
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", getAcceptHeader());
        request.addHeader("User-Agent", "Gitskarios");
        request.addHeader("PRIVATE-TOKEN", getToken());
    }

    @Override
    public void log(String message) {
        Log.v("RETROFIT_LOG", message);
    }

    public String getAcceptHeader() {
        return "application/json";
    }

    private int getLinkData(Response r) {
        /*List<Header> headers = r.getHeaders();
		Map<String, String> headersMap = new HashMap<String, String>(headers.size());
		for (Header header : headers) {
			headersMap.put(header.getName(), header.getValue());
		}

		String link = headersMap.get("Link");

		if (link != null) {
			String[] parts = link.split(",");
			try {
				PaginationLink bottomPaginationLink = new PaginationLink(parts[0]);
				if (bottomPaginationLink.rel == RelType.next) {
					return bottomPaginationLink.page;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
        return -1;
    }

    public abstract class BaseInfiniteCallback<T> implements Callback<T> {

        public BaseInfiniteCallback() {

        }

        @Override
        public void success(T t, Response response) {
            int nextPage = getLinkData(response);
            response(t);
            if (nextPage != -1) {
                executePaginated(nextPage);
            } else {
                executeNext();
            }
        }

        protected abstract void executePaginated(int nextPage);

        protected abstract void executeNext();

        protected abstract void response(T t);

        public abstract void execute();

        @Override
        public void failure(RetrofitError error) {
            if (getOnResultCallback() != null) {
                getOnResultCallback().onFail(error);
            }
        }
    }
}
