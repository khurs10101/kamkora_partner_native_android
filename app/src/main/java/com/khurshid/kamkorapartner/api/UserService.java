package com.khurshid.kamkorapartner.api;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {


    //Testing
    @Multipart
    @POST("/post")
    Call<JsonObject> testPost(@Part MultipartBody.Part file);

    //users
    @POST(ApiClient.APPEND_URL_PARTNER + "signin")
    Call<JsonObject> getSignIn(@Body JsonObject object);

    @POST(ApiClient.APPEND_URL_PARTNER + "signup")
    Call<JsonObject> getSignUp(@Body JsonObject body);

    @Multipart
    @POST(ApiClient.APPEND_URL_PARTNER + "upload/avatar/{id}")
    Call<JsonObject> uploadPartnerAvatar(@Path("id") String id,
                                         @Part MultipartBody.Part part);

    @POST(ApiClient.APPEND_URL_PARTNER + "edit/{id}")
    Call<JsonObject> updatePartnersDetails(@Path("id") String partnerId,
                                           @Body JsonObject object);

    @Multipart
    @POST(ApiClient.APPEND_URL_PARTNER + "upload/id/{id}")
    Call<JsonObject> uploadPartnerIdProof(@Path("id") String userId,
                                          @Part("doc_type") RequestBody docType,
                                          @Part MultipartBody.Part part);

    @Multipart
    @POST(ApiClient.APPEND_URL_PARTNER + "upload/address/{id}")
    Call<JsonObject> uploadPartnerAddressProof(@Path("id") String userId,
                                               @Part("doc_type") RequestBody docType,
                                               @Part MultipartBody.Part part);


    //orders
    @POST(ApiClient.APPEND_URL_ORDER + "add/{id}")
    Call<JsonObject> setOrder(@Path("id") String userId,
                              @Body JsonObject object);

    @GET(ApiClient.APPEND_URL_ORDER + "{id}")
    Call<JsonObject> getOrderOfUser(@Header("x-access-token") String token,
                                    @Path("id") String userId);

    @GET(ApiClient.APPEND_URL_ORDER + "{id}")
    Call<JsonObject> getAllOrders(@Header("x-access-token") String token,
                                  @Path("id") String userId);

    @POST(ApiClient.APPEND_URL_ORDER + "cart/{id}")
    Call<JsonObject> setCartOrder(@Path("id") String userId,
                                  @Body JsonObject object);

    //address
    @GET(ApiClient.APPEND_URL_ADDRESS + "{id}")
    Call<JsonObject> getAddressOfUser(@Header("x-access-token") String token,
                                      @Path("id") String userId);

    @POST(ApiClient.APPEND_URL_ADDRESS + "add/{id}")
    Call<JsonObject> setUserAddress(@Path("id") String userId,
                                    @Body JsonObject object);

    @POST(ApiClient.APPEND_URL_ORDER + "partners")
    Call<JsonObject> getAllOrdersOfPartner(@Body JsonObject object);

    @POST(ApiClient.APPEND_URL_ORDER + "accept/{id}")
    Call<JsonObject> acceptCurrentOrder(@Path("id") String id,
                                        @Body JsonObject object);

    @POST(ApiClient.APPEND_URL_ORDER + "reject/{id}")
    Call<JsonObject> rejectCurrentOrder(@Path("id") String id,
                                        @Body JsonObject object);

    @POST(ApiClient.APPEND_URL_ORDER + "complete/{id}")
    Call<JsonObject> completeCurrentOrder(@Path("id") String id,
                                          @Body JsonObject object);
}
