package com.arziman_off.chatbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String LOG_TAG = "NeedAppLogs";
    private static final String EXTRA_EMAIL = "email";
    private EditText etEmailReset;
    private ImageView btnGoBack;
    private MaterialButton btnResetPassword;
    private ResetPasswordViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        String emailToReset = getIntent().getStringExtra(EXTRA_EMAIL);
        etEmailReset.setText(emailToReset);
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        setupClickListeners();
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getLinkIsSend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean linkIsSend) {
                if (linkIsSend){
                    finish();
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            "Ссылка для сброса пароля отправленна на указанную почту",
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(LOG_TAG, "Ссылка для сброса пароля отправленна на указанную почту");
                }
            }
        });
        viewModel.getExceptionText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String exceptionText) {
                if (exceptionText != null) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            exceptionText,
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(LOG_TAG, exceptionText.toString());
                }
            }
        });
    }

    private void setupClickListeners() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailReset.getText().toString().trim();
                viewModel.resetPassword(email);
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initViews() {
        etEmailReset = findViewById(R.id.etEmailReset);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnGoBack = findViewById(R.id.btnGoBack);
    }

    public static Intent newIntent(Context context, String email){
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

}