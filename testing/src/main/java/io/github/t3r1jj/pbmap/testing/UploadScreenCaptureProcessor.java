package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UploadScreenCaptureProcessor implements ScreenCaptureProcessor {
    private static final String TAG = UploadScreenCaptureProcessor.class.getSimpleName();

    @Override
    public String process(ScreenCapture capture) throws IOException {
        byte[] imageDate = getImageData(capture);
        Call<ResponseBody> call = uploadImageData(capture, imageDate);
        Response<ResponseBody> response = call.execute();
        ResponseBody body = response.isSuccessful() ? response.body() : response.errorBody();
        String result = getResult(response, body);
        Log.println(response.isSuccessful() ? Log.INFO : Log.ERROR, TAG, result);
        return result;
    }

    private String getResult(Response<ResponseBody> response, ResponseBody body) throws IOException {
        return body == null ? response.message() : body.string();
    }

    private Call<ResponseBody> uploadImageData(ScreenCapture capture, byte[] data) {
        UploadService service = ServiceGenerator.createService(UploadService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), data);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", capture.getName(), requestFile);
        return service.upload(body);
    }

    private byte[] getImageData(ScreenCapture capture) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        capture.getBitmap().compress(capture.getFormat(), 100, outputStream);
        outputStream.close();
        return outputStream.toByteArray();
    }
}
