package vineture.wowhubb.Utils;


import vineture.wowhubb.FeedsData.Feeds;
import vineture.wowhubb.Groups.GroupData;
import vineture.wowhubb.MyFeedsData.MyFeeds;
import vineture.wowhubb.Profile.Profile;
import vineture.wowhubb.todayevents.Main;

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

    @POST("todaysfeed")
    Call<Main> getTodayEvents(@Header("Content-Type") String contenttype,
                                @Header("token") String token);
    @POST("fetchgroups")
    Call<GroupData> getGroups(@Header("Content-Type") String contenttype,
                              @Header("token") String token);


    @POST("getpersonalprofile")
    Call<Profile> getProfile(@Header("Content-Type") String contenttype,
                             @Header("token") String token);
}
