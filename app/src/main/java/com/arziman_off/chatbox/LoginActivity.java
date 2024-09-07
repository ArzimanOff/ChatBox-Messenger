package com.arziman_off.chatbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivityLogs";

    private EditText etEmailLogin;
    private EditText etPasswordLogin;
    private TextView tvForgotPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnGoToRegistration;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString().trim();
                String password = etPasswordLogin.getText().toString().trim();
                viewModel.login(email, password);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ResetPasswordActivity.newIntent(
                        LoginActivity.this,
                        etEmailLogin.getText().toString().trim()
                );
                startActivity(intent);
            }
        });

        btnGoToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegisterActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    private void observeViewModel(){
        viewModel.getExceptionText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String exceptionText) {
                if (exceptionText != null) {
                    Toast.makeText(
                            LoginActivity.this,
                            exceptionText,
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(LOG_TAG, exceptionText.toString());
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null){
                    //TODO открытие экрана чатов
                    Toast.makeText(
                            LoginActivity.this,
                            "Пользователь авторизован",
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(LOG_TAG, "Пользователь авторизован " + firebaseUser.getUid());
                }
            }
        });
    }

    private void initViews() {
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegistration = findViewById(R.id.btnGoToRegistration);
    }


}