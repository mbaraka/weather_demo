package com.weather.utils;

import android.util.Log;

import io.reactivex.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RequestHelper {
	private OkHttpClient client = new OkHttpClient();

	public Single<String> request(final String url) {
		return Single.create((SingleOnSubscribe<String>) emitter -> {
			Log.i("RequestHelper", url);
			Request request = new Request.Builder()
					.url(url)
					.build();
			try {
				Response response = client.newCall(request).execute();
				if (!response.isSuccessful()) {
					emitter.onError(new Exception(response.body().string()));
				} else if (response.body() != null) {
					emitter.onSuccess(response.body().string());
				} else {
					emitter.onError(new NullPointerException("no body from request"));
				}
			} catch (IOException | NullPointerException ex) {
				emitter.onError(ex);
			}
		})
				.compose(RxHelper.INSTANCE.<String>asyncToUiSingle());
	}

}
