package com.arziman_off.chatbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String LOG_TAG = "NeedAppLogs";
    private static final String EXTRA_CURRENT_USER_ID = "current_user_id";
    private static final String EXTRA_OTHER_USER_ID = "other_user_id";
    private ImageView btnCloseChat;
    private View userAvatar;
    private View onlineStatusBox;
    private TextView userNameBox;
    private TextView userOtherInfoBox;
    private EditText etNewMessageInput;
    private ImageView btnSendMessage;
    private RecyclerView rvMessages;
    private MessagesAdapter messagesAdapter;
    private String currentUserId;
    private String otherUserId;
    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);

        initViews();
        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        messagesAdapter = new MessagesAdapter(currentUserId);
        rvMessages.setAdapter(messagesAdapter);

        setupEventListeners();
        observeViewModel();
    }

    private void setupEventListeners(){

        // Добавляем TextWatcher для отслеживания изменений текста
        etNewMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // До изменения текста
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Когда текст изменяется
                String inputText = charSequence.toString().trim(); // Убираем пробелы в начале и в конце

                if (!inputText.isEmpty()) {
                    // Если текст введен, меняем иконку на активную
                    btnSendMessage.setImageResource(R.drawable.active_send_icon);
                } else {
                    // Если поле пустое (или только пробелы), возвращаем неактивную иконку
                    btnSendMessage.setImageResource(R.drawable.inactive_send_icon);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // После изменения текста
            }
        });
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etNewMessageInput.getText().toString().trim().isEmpty()){
                    Message message = new Message(
                            etNewMessageInput.getText().toString().trim(),
                            currentUserId,
                            otherUserId
                    );
                    viewModel.sendMessage(message);
                }
            }
        });
        btnCloseChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void observeViewModel(){
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null){
                    Log.d(LOG_TAG, errorMsg);
                    Toast.makeText(
                            ChatActivity.this,
                            errorMsg,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
        viewModel.getMessageIsSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent) {
                    etNewMessageInput.setText("");
                }
            }
        });
        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userNameBox.setText(user.getName());
                userOtherInfoBox.setText(String.valueOf(user.getAge()));
            }
        });
    }

    private void initViews() {
        btnCloseChat = findViewById(R.id.btnCloseChat);
        userAvatar = findViewById(R.id.userAvatar);
        onlineStatusBox = findViewById(R.id.onlineStatusBox);
        userNameBox = findViewById(R.id.userNameBox);
        userOtherInfoBox = findViewById(R.id.userOtherInfoBox);
        rvMessages = findViewById(R.id.rvMessages);
        etNewMessageInput = findViewById(R.id.etNewMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}