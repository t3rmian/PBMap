package io.github.t3r1jj.pbmap.test;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

class ServiceGenerator {

    private static final String BASE_URL = "https://file.io";

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    @SuppressWarnings("SameParameterValue")
    static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
