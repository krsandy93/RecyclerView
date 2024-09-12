package com.alkyeservices.recyclerviewexample.ViewModel;

// UserViewModel.java
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alkyeservices.recyclerviewexample.model.UserModel;
import com.alkyeservices.recyclerviewexample.repo.UserRepository;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<UserModel> users;

    public UserViewModel() {
        userRepository = new UserRepository();
        users = new MutableLiveData<>();

    }

    public LiveData<UserModel> getUsers(int page) {
        fetchUsers(page);
        return users;
    }

    private void fetchUsers(int page) {
        userRepository.getUsers(page).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("Sandeep Error", t.getMessage());
                // Handle failure
            }
        });
    }
}

