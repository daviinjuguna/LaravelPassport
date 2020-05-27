package com.example.laravelpassport.network;



import com.example.laravelpassport.entities.AccessTokens;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<AccessTokens> register(
            @Field("name")String name,
            @Field("email")String email,
            @Field("password")String password
    );

}
