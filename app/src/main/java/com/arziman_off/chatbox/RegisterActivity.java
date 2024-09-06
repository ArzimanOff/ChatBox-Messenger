package com.arziman_off.chatbox;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private int userAge;
    private EditText etEmailRegister;
    private EditText etPasswordRegister;
    private EditText etNameRegister;
    private EditText etLastNameRegister;
    private EditText etDateOfBirthRegister;
    private MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getInfoFrom(etEmailRegister);
                String password = getInfoFrom(etPasswordRegister);
                String name = getInfoFrom(etNameRegister);
                String lastName = getInfoFrom(etLastNameRegister);
                String dateOfBirth = getInfoFrom(etDateOfBirthRegister);
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
        etDateOfBirthRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAge = getDateOfBirth();
            }
        });
        btnRegister = findViewById(R.id.btnRegister);
    }

    private int getDateOfBirth(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
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
        return new Intent(context, RegisterActivity.class);
    }
}