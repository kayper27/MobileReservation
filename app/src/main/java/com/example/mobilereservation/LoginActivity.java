package com.example.mobilereservation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilereservation.view.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, loginFragment).commit();
        }
    }
}