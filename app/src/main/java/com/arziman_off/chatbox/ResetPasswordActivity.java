package com.arziman_off.chatbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String EXTRA_EMAIL = "email";

    private EditText etEmailReset;
    private MaterialButton btnResetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        String emailToReset = getIntent().getStringExtra(EXTRA_EMAIL);
        etEmailReset.setText(emailToReset);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailReset.getText().toString().trim();
                // reset password
            }
        });
    }

    private void initViews() {
        etEmailReset = findViewById(R.id.etEmailReset);
        btnResetPassword = findViewById(R.id.btnResetPassword);
    }

    public static Intent newIntent(Context context, String email){
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

}