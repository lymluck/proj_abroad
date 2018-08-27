package com.smartstudy.commonlib.base.config.https;

import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Fetches an {@link InputStream} using the okhttp library.
 */
public class OkHttpStreamFetcher implements DataFetcher<InputStream> {
    private final OkHttpClient client;
    private final GlideUrl url;
    private InputStream stream;
    private ResponseBody responseBody;
    private volatile Call call;

    public OkHttpStreamFetcher(OkHttpClient client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull final DataCallback<? super InputStream> callback) {
        Request.Builder requestBuilder = new Request.Builder()
            .url(url.toStringUrl());

        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }

        Request request = requestBuilder.build();
        this.call = this.client.newCall(request);
        this.call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onLoadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OkHttpStreamFetcher.this.responseBody = response.body();
                if (response.isSuccessful()) {
                    long contentLength = OkHttpStreamFetcher.this.responseBody.contentLength();
                    OkHttpStreamFetcher.this.stream = ContentLengthInputStream.obtain(OkHttpStreamFetcher.this.responseBody.byteStream(), contentLength);
                    callback.onDataReady(OkHttpStreamFetcher.this.stream);
                } else {
                    callback.onLoadFailed(new HttpException(response.message(), response.code()));
                }
            }
        });
    }

    @Override
    public void cleanup() {
        try {
            if (this.stream != null) {
                this.stream.close();
            }
        } catch (IOException e) {
        }
        if (responseBody != null) {
            responseBody.close();
        }
    }

    @Override
    public void cancel() {
        Call local = this.call;
        if (local != null) {
            local.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.DATA_DISK_CACHE;
    }
}
