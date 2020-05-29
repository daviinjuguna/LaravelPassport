package com.example.laravelpassport.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.laravelpassport.PostActivity;
import com.example.laravelpassport.R;
import com.example.laravelpassport.AuthInterface;
import com.example.laravelpassport.TokenInterface;
import com.example.laravelpassport.TokenManager;
import com.example.laravelpassport.Utils;
import com.example.laravelpassport.entities.AccessTokens;
import com.example.laravelpassport.entities.ApiError;
import com.example.laravelpassport.network.ApiService;
import com.example.laravelpassport.network.RetrofitBuilder;
import com.google.android.material.textfield.TextInputLayout;


import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;


/**
 */
public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";

    @BindView(R.id.txtLayoutNameSignUp)
    TextInputLayout txtName;
    @BindView(R.id.txtLayoutEmailSignUp)
    TextInputLayout txtEmail;
    @BindView(R.id.txtLayoutPasswordSignUp)
    TextInputLayout txtPassword;
    @BindView(R.id.txtConfirmLayoutPasswordSignUp)
    TextInputLayout txtConfirmPassword;

    ApiService service;
    Call<AccessTokens> call;

    AwesomeValidation validation;

    TokenManager tokenManager;
    private RetrofitBuilder retrofitBuilder;

    private AuthInterface authInterface;
    private TokenInterface tokenInterface;

    private ProgressDialog progressDialog;

    public SignUpFragment(AuthInterface authInterface, TokenInterface tokenInterface) {
        this.authInterface = authInterface;
        this.tokenInterface = tokenInterface;
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this,view);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        tokenManager = tokenInterface.getInstance();
        setUpRules();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);



        //save login and register
        if (tokenManager.getToken().getAccessToken() !=null){
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnSignUp)
    void register(){

        String name = txtName.getEditText().getText().toString();
        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();
        String password_confirmation = txtConfirmPassword.getEditText().getText().toString();

        txtName.setError(null);
        txtEmail.setError(null);
        txtPassword.setError(null);
        txtConfirmPassword.setError(null);

        validation.clear();

        if (validation.validate()) {

           // call = service.register(name, email, password);
            call = RetrofitBuilder.apiService().register(name, email, password, password_confirmation);
            progressDialog.show();
            call.enqueue(new Callback<AccessTokens>() {
                @Override
                public void onResponse(Call<AccessTokens> call, Response<AccessTokens> response) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        tokenManager.saveToken(response.body());
                        Intent intent = new Intent(getActivity(), PostActivity.class);
                        startActivity(intent);

                    } else {
                        progressDialog.dismiss();
                        handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AccessTokens> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });
        }
    }

    @OnClick(R.id.txtSignIn)
    void login(){
        authInterface.goToLogin();
    }

    private void handleErrors(ResponseBody response){
        ApiError apiError = Utils.convertErrors(response);

        for (Map.Entry<String, List<String>> error:apiError.getErrors().entrySet()){
            if (error.getKey().equals("name")){
                txtName.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("email")){
                txtEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")){
                txtPassword.setError(error.getValue().get(0));
            }
        }
    }

    public void setUpRules(){
        validation.addValidation(getActivity(),R.id.txtLayoutNameSignUp,RegexTemplate.NOT_EMPTY,R.string.error_name);
        validation.addValidation(getActivity(),R.id.txtLayoutEmailSignUp, Patterns.EMAIL_ADDRESS,R.string.error_email);
        validation.addValidation(getActivity(),R.id.txtLayoutPasswordSignUp,"[a-zA-z0-9]{6,}",R.string.error_password);
        validation.addValidation(getActivity(),R.id.txtConfirmLayoutPasswordSignUp,"[a-zA-z0-9]{6,}",R.string.error_password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call !=null){
            call.cancel();
            call = null;
        }
    }
}
