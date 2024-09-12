package com.arziman_off.chatbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SEND_MESSAGE = 100;
    private static final int VIEW_TYPE_RECEIVED_MESSAGE = 101;
    private String currentUserId;
    private List<Message> messages = new ArrayList<>();
    private OnNeedScrollDownListener onNeedScrollDownListener;

    public void setOnNeedScrollDownListener(OnNeedScrollDownListener onNeedScrollDownListener) {
        this.onNeedScrollDownListener = onNeedScrollDownListener;
    }

    public MessagesAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if (viewType == VIEW_TYPE_SEND_MESSAGE){
            layoutResId = R.layout.sent_message_view;
        } else {
            layoutResId = R.layout.received_message_view;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(
                layoutResId,
                parent,
                false
        );
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SEND_MESSAGE;
        } else {
            return VIEW_TYPE_RECEIVED_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvMessageText.setText(message.getText());
        holder.tvMessageDate.setText(message.getSentAt());

        if (onNeedScrollDownListener != null){
            onNeedScrollDownListener.onScrollDown(position < (messages.size() - 6));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    interface OnNeedScrollDownListener{
        void onScrollDown(boolean mode);
    }
    static class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMessageText;
        private TextView tvMessageDate;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            tvMessageDate = itemView.findViewById(R.id.tvMessageDate);
        }
    }
}
