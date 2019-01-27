package com.homework.grop.group_homework.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface IMiniDouyinService {
    String HOST = "http://10.108.10.39:8080/";

    @Multipart
    @POST("/minidouyin/video")
    Call<PostVideoResponse> createVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video);

    @GET("/minidouyin/feed")
    Call<FeedResponse> fetchFeed();
}
