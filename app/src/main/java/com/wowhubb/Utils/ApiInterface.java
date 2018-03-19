package com.wowhubb.Utils;


import com.wowhubb.FeedsData.Feeds;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ITSoftSupport on 29-12-2017.
 */

public interface ApiInterface {

@FormUrlEncoded
    @POST("androidfeed")
    Call<Feeds> checkLogin(
        @Header("Content-Type") String contenttype,
        @Header("token") String token,
        @Field("page") int pageIndex);


    //Ramya ji if header is constant ,, use this

    /*@Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Accept: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("recceeList")
    Call<TestInfo> testRequest2(@Field("userid") String userid);*/


    // if header is dynamic,,, use this


/*
    @POST("recceeList")
    Call<ResponseBody> testRequest1(@Body TestModel body,
                                          @Header("Content-Type") String contenttype,
                                          @Header("X-CSRF-TOKEN") String accept);
*/


}
