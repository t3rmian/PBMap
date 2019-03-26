package io.github.t3r1jj.pbmap.sample.integration;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class UploadScreenCaptureProcessor implements ScreenCaptureProcessor {

    @Override
    public String process(ScreenCapture capture) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        capture.getBitmap().compress(capture.getFormat(), 100, outputStream);
        outputStream.flush();
        TransferSh service =
                ServiceGenerator.createService(TransferSh.class);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image"), outputStream.toByteArray());
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", capture.getName(), requestFile);
        outputStream.close();
        Call<ResponseBody> call = service.upload(body);
//        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call,
//                                   Response<ResponseBody> response) {
//                Log.i("Upload", response.message());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload", t.getMessage());
//            }
//        };

        Log.i("Upload", "processing2");
        Response<ResponseBody> response = call.execute();
        Log.i("Upload", "finished");
        if (response.isSuccessful()) {
            Log.i("Upload", response.body().string());
        } else {
            Log.e("Upload", response.errorBody().string());
        }
//        call.enqueue(callback);
        return null;
    }

    public static class ServiceGenerator {

        private static final String BASE_URL = "https://file.io";

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL);

        private static OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        private static Retrofit retrofit = builder.client(httpClient.build()).build();

        public static <S> S createService(
                Class<S> serviceClass) {
            return retrofit.create(serviceClass);
        }
    }

    public interface TransferSh {
        @Multipart
        @POST("/")
        Call<ResponseBody> upload(@Part MultipartBody.Part file);
    }
}
