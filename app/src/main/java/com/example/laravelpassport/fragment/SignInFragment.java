package com.example.laravelpassport.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.laravelpassport.PostActivity;
import com.example.laravelpassport.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInFragment extends Fragment {
    private static final String TAG = "SignInFragment";

    @BindView(R.id.txtLayoutEmailSignIn)
    TextInputLayout txtEmail;
    @BindView(R.id.txtLayoutPasswordSignIn)
    TextInputLayout txtPassword;

    ApiService service;
    TokenManager tokenManager;
    AwesomeValidation validation;
    Call<AccessTokens> call;

    private TokenInterface tokenInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = tokenInterface.getInstance();
        validation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setUpRules();

        //save login and register
        if (tokenManager.getToken().getAccessToken() !=null){
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        }
    }
    @OnClick(R.id.btnSignIn)
    void login(){

        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        txtEmail.setError(null);
        txtPassword.setError(null);

        validation.clear();

        if (validation.validate()) {

            call = service.login(email, password);
            call.enqueue(new Callback<AccessTokens>() {
                @Override
                public void onResponse(Call<AccessTokens> call, Response<AccessTokens> response) {
                    if (response.isSuccessful()) {
                        tokenManager.saveToken(response.body());
                        Intent intent = new Intent(getActivity(), PostActivity.class);
                        startActivity(intent);

                    } else {
                        if (response.code() == 422) {
                            handleErrors(response.errorBody());
                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.convertErrors(response.errorBody());
                            Toast.makeText(getActivity(), apiError.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<AccessTokens> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });
        }
    }

    @OnClick(R.id.txtSignUp)
    void register(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignUpFragment()).commit();
    }

    private void handleErrors(ResponseBody response){
        ApiError apiError = Utils.convertErrors(response);

        for (Map.Entry<String, List<String>> error:apiError.getErrors().entrySet()){
            if (error.getKey().equals("username")){
                txtEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")){
                txtPassword.setError(error.getValue().get(0));
            }
        }
    }

    public void setUpRules(){
        validation.addValidation(getActivity(),R.id.txtLayoutEmailSignUp, Patterns.EMAIL_ADDRESS,R.string.error_email);
        validation.addValidation(getActivity(),R.id.txtLayoutPasswordSignUp,RegexTemplate.NOT_EMPTY,R.string.error_password);
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
