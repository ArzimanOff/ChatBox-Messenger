package com.arziman_off.chatbox;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
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
    private static final String EXTRA_CURRENT_USER_ID = "current_user_id";
    private static final String LOG_TAG = "NeedAppLogs";
    private UsersViewModel viewModel;
    private ImageView btnLogOut;
    private RecyclerView rvUsersChats;
    private UsersChatsAdapter usersChatsAdapter;
    private String currentUserId;
    private LinearLayout allUsersListEmptyPlaceholderBox;
    private LottieAnimationView placeholderAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        setupClickListeners();
        observeViewModel();
    }

    private void initViews() {
        btnLogOut = findViewById(R.id.btnLogOut);
        rvUsersChats = findViewById(R.id.rvUserChatsList);
        allUsersListEmptyPlaceholderBox = findViewById(R.id.allUsersListEmptyPlaceholderBox);
        placeholderAnim = findViewById(R.id.allUsersListEmptyPlaceholderAnimation);
        placeholderAnim.setRepeatCount(LottieDrawable.INFINITE);
        placeholderAnim.setRepeatMode(LottieDrawable.RESTART);
        placeholderAnim.playAnimation();

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
        usersChatsAdapter.setOnUserChatClickListener(new UsersChatsAdapter.OnUserChatClickListener() {
            @Override
            public void onUserChatClick(User user) {
                Intent intent = ChatActivity.newIntent(
                        UsersActivity.this,
                        currentUserId,
                        user.getId()
                );
                startActivity(intent);
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
        viewModel.getUsersList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> usersList) {
                if (!usersList.isEmpty()){
                    allUsersListEmptyPlaceholderBox.setVisibility(View.GONE);
                    rvUsersChats.setVisibility(View.VISIBLE);

                    usersChatsAdapter.setUsers(usersList);
                } else {
                    allUsersListEmptyPlaceholderBox.setVisibility(View.VISIBLE);
                    rvUsersChats.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnlineStatus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnlineStatus(false);
    }

    public static Intent newIntent(Context context, String currentUserId){
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
    }
}