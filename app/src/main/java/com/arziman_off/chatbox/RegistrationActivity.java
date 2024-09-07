package com.arziman_off.chatbox;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = "NeedAppLogs";
    private int userAge;
    private EditText etEmailRegister;
    private EditText etPasswordRegister;
    private EditText etNameRegister;
    private EditText etLastNameRegister;
    private EditText etDateOfBirthRegister;
    private MaterialButton btnRegister;
    private ImageView btnGoBack;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        setupClickListeners();
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getExceptionText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String exceptionText) {
                if (exceptionText != null) {
                    Toast.makeText(
                            RegistrationActivity.this,
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
                    Log.d(LOG_TAG, "Пользователь авторизован " + firebaseUser.getUid());
                    Intent intent = UsersActivity.newIntent(RegistrationActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getInfoFrom(etEmailRegister);
                String password = getInfoFrom(etPasswordRegister);
                String name = getInfoFrom(etNameRegister);
                String lastName = getInfoFrom(etLastNameRegister);
                String dateOfBirth = getInfoFrom(etDateOfBirthRegister);

                viewModel.signUp(
                        email,
                        password,
                        name,
                        lastName,
                        dateOfBirth,
                        userAge
                );
            }
        });
        etDateOfBirthRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAge = getDateOfBirth();
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getInfoFrom(EditText et) {
        return et.getText().toString().trim();
    }

    private void initViews() {
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etNameRegister = findViewById(R.id.etNameRegister);
        etLastNameRegister = findViewById(R.id.etLastNameRegister);
        etDateOfBirthRegister = findViewById(R.id.etDateOfBirthRegister);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoBack = findViewById(R.id.btnGoBack);
    }

    private int getDateOfBirth(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Устанавливаем выбранную дату в EditText
                        etDateOfBirthRegister.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
        return calculateAge(year, month, day);
    }

    private int calculateAge(int year, int month, int day) {
        // Текущая дата
        Calendar today = Calendar.getInstance();

        // Дата рождения
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month, day);

        // Вычисляем разницу в годах
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // Проверяем, был ли уже день рождения в этом году
        if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    public static Intent newIntent(Context context){
        return new Intent(context, RegistrationActivity.class);
    }
}