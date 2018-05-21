package com.wowhubb.Utils;


import com.wowhubb.FeedsData.Feeds;
import com.wowhubb.Groups.GroupData;
import com.wowhubb.MyFeedsData.MyFeeds;
import com.wowhubb.todayevents.Main;
import com.wowhubb.todayevents.Message;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @POST("todaysfeed")
    Call<Main> getTodayEvents(@Header("Content-Type") String contenttype,
                                @Header("token") String token);
    @POST("fetchgroups")
    Call<GroupData> getGroups(@Header("Content-Type") String contenttype,
                              @Header("token") String token);
}
