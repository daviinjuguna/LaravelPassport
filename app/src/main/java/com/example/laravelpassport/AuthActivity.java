package com.example.laravelpassport;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.example.laravelpassport.fragment.SignInFragment;
import com.example.laravelpassport.fragment.SignUpFragment;

public class AuthActivity extends AppCompatActivity implements AuthInterface, TokenInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment(this,this)).commit();
    }

    @Override
    public TokenManager getInstance() {
        return TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment(this, this)).commit();
    }

    @Override
    public void goToRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignUpFragment(this, this)).commit();
    }
}
