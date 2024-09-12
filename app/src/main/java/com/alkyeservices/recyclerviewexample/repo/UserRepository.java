package com.alkyeservices.recyclerviewexample.repo;

// UserRepository.java

import com.alkyeservices.recyclerviewexample.apiService.ApiService;
import com.alkyeservices.recyclerviewexample.apiService.RetrofitClient;
import com.alkyeservices.recyclerviewexample.model.UserModel;

import java.util.List;

import retrofit2.Call;

public class UserRepository {
    private ApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public Call<UserModel> getUsers(int page) {
        return apiService.getUsers(page);
    }
}
