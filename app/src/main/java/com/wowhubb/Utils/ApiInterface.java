package com.wowhubb.Utils;


import com.wowhubb.FeedsData.Feeds;
import com.wowhubb.MyFeedsData.MyFeeds;

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


    @FormUrlEncoded
    @POST("myfeeds")
    Call<MyFeeds> checkFeeds(
            @Header("Content-Type") String contenttype,
            @Header("token") String token,
    @Field("page") int pageIndex);


}
