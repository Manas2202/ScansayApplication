package com.example.scansayapplication;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RetrofitInterface {
    @POST("/userlogin/request")
    Call<Void> executeLoginData(@Body HashMap<String , String> map);

    @POST("/userdata/request")
    Call<Void> executeUserData(@Body HashMap<String , String> map);

    @POST("/loggedinuser/response")
    Call<Void> executeLoggedInUserData(@Body HashMap<String , String> map);

    @POST("/otp/request")
    Call<Void> executeOtpData(@Body HashMap<String , String> map);

    @POST("/update/request")
    Call<Void> executeUpdateProcess(@Body HashMap<String , String> map);

    @POST("/paymentdata/response")
    Call<List<PaymentListModel>> fetchPaymentListData(@Body HashMap<String , String> map);

    @POST("/loginOtpCheck/responses")
    Call<List<LoginResponseData>> executeLoginOtpCheckData(@Body HashMap<String , String> map);

    @POST("/paymentdata/request")
    Call<Void> executePaymentSendRequest(@Body HashMap<String , String> map);

    @Multipart
    @POST("/upload")
    Call<Void> uploadFile(@Part MultipartBody.Part file, @Part("upload") RequestBody name);

    @POST("/api/response")
    Call<List<Responsedata>> checkingPost(@Body HashMap<String , String> map);
}
