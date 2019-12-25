package io.github.t3r1jj.pbmap.testing;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

final class ServiceGeneratorUtils {

    private static final String BASE_URL = "https://file.io";

    private static final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);

    private static final OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    private static final Retrofit retrofit = builder.client(httpClient.build()).build();

    private ServiceGeneratorUtils() {
    }

    @SuppressWarnings("SameParameterValue")
    static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
