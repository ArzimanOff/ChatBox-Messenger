package com.arziman_off.chatbox;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsersActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NeedAppLogs";
    private UsersViewModel viewModel;
    private ImageView btnLogOut;
    private RecyclerView rvUsersChats;
    private UsersChatsAdapter usersChatsAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = firebaseDatabase.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        for (int i = 0; i < 20; i++) {
            User user = new User(
                    "id" + i,
                    "name" + i,
                    "lastName" + i,
                    "09/03/2022",
                    i,
                    new Random().nextBoolean()
            );
            dbRef.push().setValue(user);
        }

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Log.d(LOG_TAG, dataSnapshot.getValue(User.class).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initViews();
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        setupClickListeners();
        observeViewModel();

        List<User> users = new ArrayList<>();

        usersChatsAdapter.setUsers(users);
    }

    private void initViews() {
        btnLogOut = findViewById(R.id.btnLogOut);
        rvUsersChats = findViewById(R.id.rvUserChatsList);
        usersChatsAdapter = new UsersChatsAdapter();
        rvUsersChats.setAdapter(usersChatsAdapter);
    }
    private void setupClickListeners(){
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null){
                    Intent intent = LoginActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        // Создаем MaterialAlertDialog
        new MaterialAlertDialogBuilder(this)
                .setTitle("Подтверждение выхода")
                .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                .setPositiveButton("Да, выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Логика выхода из аккаунта
                        viewModel.logout();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрываем диалог
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, UsersActivity.class);
        return intent;
    }
}