package io.github.t3r1jj.pbmap.test;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface TransferSh {
    @Multipart
    @POST("/")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);
}
