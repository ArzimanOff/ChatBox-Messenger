package com.arziman_off.chatbox;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.Format;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String LOG_TAG = "NeedAppLogs";
    private static final String EXTRA_CURRENT_USER_ID = "current_user_id";
    private static final String EXTRA_OTHER_USER_ID = "other_user_id";
    private ImageView btnCloseChat;
    private ImageView btnChatOptions;
    private View userAvatar;
    private View onlineStatusBox;
    private TextView userNameBox;
    private TextView userOtherInfoBox;
    private EditText etNewMessageInput;
    private ImageView btnSendMessage;
    private ImageView btnScrollToDown;
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

        btnChatOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        btnScrollToDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
            }
        });
        messagesAdapter.setOnNeedScrollDownListener(new MessagesAdapter.OnNeedScrollDownListener() {
            @Override
            public void onScrollDown(boolean mode) {
                if (!mode){
                    btnScrollToDown.setVisibility(View.GONE);
                } else {
                    btnScrollToDown.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void observeViewModel(){
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
                rvMessages.scrollToPosition(messages.size() - 1);
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
                userNameBox.setText(
                        String.format("%s %s", user.getName(), user.getLastName())
                );
                userOtherInfoBox.setText(String.valueOf(user.getAge()));
                setOnlineStatus(onlineStatusBox, user.isOnlineStatus());
            }
        });
    }

    private void setOnlineStatus(View onlineStatusBox, boolean onlineStatus) {
        if (onlineStatus){
            onlineStatusBox.setBackgroundResource(R.drawable.online);
        } else {
            onlineStatusBox.setBackgroundResource(R.drawable.offline);
        }
    }

    private void initViews() {
        btnCloseChat = findViewById(R.id.btnCloseChat);
        btnChatOptions = findViewById(R.id.btnChatOptions);
        btnScrollToDown = findViewById(R.id.btnScrollToDown);
        userAvatar = findViewById(R.id.userAvatar);
        onlineStatusBox = findViewById(R.id.onlineStatusBox);
        userNameBox = findViewById(R.id.userNameBox);
        userOtherInfoBox = findViewById(R.id.userOtherInfoBox);
        rvMessages = findViewById(R.id.rvMessages);
        etNewMessageInput = findViewById(R.id.etNewMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);
    }

    // Метод для отображения программно созданного PopupMenu
    private void showPopupMenu(View view) {

        // Создаем объект PopupMenu
        PopupMenu popupMenu = new PopupMenu(ChatActivity.this, view);

        // Программно добавляем элементы меню
        popupMenu.getMenu().add(0, 1, 0, "Поиск сообщения").setIcon(R.drawable.search_icon);  // Элемент 1 с иконкой
        popupMenu.getMenu().add(0, 2, 1, "Удалить чат").setIcon(R.drawable.trash_icon);  // Элемент 2 с иконкой

        // Указываем отображение иконок в меню
        popupMenu.setForceShowIcon(true);

        // Обработчик кликов по элементам меню
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        // Действие для первого элемента
                        Toast.makeText(
                                ChatActivity.this,
                                "Выбрано поиск сообщения",
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    case 2:
                        // Действие для второго элемента
                        showDeleteMessagesConfirmationDialog();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Показываем меню
        popupMenu.show();
    }

    private void showDeleteMessagesConfirmationDialog() {
        // Инфлейтим кастомный макет
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialod_messages_delete, null);

        // Получаем CheckBox из кастомного вида
        CheckBox cbDeleteForReceiver = dialogView.findViewById(R.id.cbDeleteForReceiver);

        // Создаем MaterialAlertDialog
        new MaterialAlertDialogBuilder(this)
                .setTitle("Подтверждение удаления")
                .setView(dialogView)  // Устанавливаем кастомный вид
                .setPositiveButton("Да, удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deleteForReceiver = cbDeleteForReceiver.isChecked(); // Получаем состояние CheckBox

                        if (deleteForReceiver) {
                            viewModel.deleteChat(true);
                            Toast.makeText(
                                    ChatActivity.this,
                                    "Сообщения удалены для всех!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            viewModel.deleteChat(false);
                            Toast.makeText(
                                    ChatActivity.this,
                                    "Сообщения удалены для вас!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
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

    public static Intent newIntent(Context context, String currentUserId, String otherUserId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }

}