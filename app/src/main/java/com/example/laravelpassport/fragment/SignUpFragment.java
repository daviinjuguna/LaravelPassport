package com.example.laravelpassport.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laravelpassport.R;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    @BindView(R.id.txtLayoutNameSignUp)
    TextInputLayout txtName;
    @BindView(R.id.txtLayoutEmailSignUp)
    TextInputLayout txtEmail;
    @BindView(R.id.txtLayoutPasswordSignUp)
    TextInputLayout txtPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick(R.id.btnSignUp)
    void register(){}
}
