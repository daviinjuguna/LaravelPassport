package com.example.laravelpassport;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.example.laravelpassport.fragment.SignUpFragment;

public class AuthActivity extends AppCompatActivity implements TokenInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignUpFragment()).commit();
    }

    @Override
    public TokenManager getInstance() {
        return TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
    }
}
