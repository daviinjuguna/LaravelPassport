package com.example.laravelpassport;

import android.content.SharedPreferences;

import com.example.laravelpassport.entities.AccessTokens;

public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE=null;

    public TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if (INSTANCE==null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

   public void saveToken(AccessTokens tokens){
        editor.putString("ACCESS_TOKEN",tokens.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN",tokens.getRefreshToken()).commit();
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
    }

    public AccessTokens getToken(){
        AccessTokens tokens = new AccessTokens();
        tokens.setAccessToken(prefs.getString("ACCESS_TOKEN",null));
        tokens.setRefreshToken(prefs.getString("REFRESH_TOKEN",null));
        return tokens;
    }
}
