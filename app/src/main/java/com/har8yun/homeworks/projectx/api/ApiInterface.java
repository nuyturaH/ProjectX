package com.har8yun.homeworks.projectx.api;

import com.har8yun.homeworks.projectx.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
//            @Query("sources") String sources,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}
