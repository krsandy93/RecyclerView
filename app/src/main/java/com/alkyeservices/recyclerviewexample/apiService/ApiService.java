package com.alkyeservices.recyclerviewexample.apiService;

// ApiService.java
import com.alkyeservices.recyclerviewexample.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @GET("users")
    Call<UserModel> getUsers(@Query("page") int page);
}

