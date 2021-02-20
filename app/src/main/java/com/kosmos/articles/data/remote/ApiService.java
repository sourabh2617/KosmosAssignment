package com.kosmos.articles.data.remote;

import com.kosmos.articles.data.remote.model.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ApiService {

@GET("users")
Call<UserData> loadPopularArticles(@Query("page") int index);
}
