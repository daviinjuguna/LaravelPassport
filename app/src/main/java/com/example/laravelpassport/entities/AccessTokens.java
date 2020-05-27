package com.example.laravelpassport.entities;

import com.squareup.moshi.Json;

public class AccessTokens {

    @Json(name = "token_type")
    String tokenType;
    @Json(name = "expires_in")
    int expiresIn;
    @Json(name = "access_token")
    String accessToken;
    @Json(name = "refresh_token")
    String refreshToken;
}
