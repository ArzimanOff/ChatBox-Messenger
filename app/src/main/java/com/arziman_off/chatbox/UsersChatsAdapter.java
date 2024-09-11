package com.arziman_off.chatbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersChatsAdapter extends RecyclerView.Adapter<UsersChatsAdapter.UsersChatsViewHolder>{

    private List<User> users = new ArrayList<>();
    private OnUserChatClickListener onUserChatClickListener;

    public void setOnUserChatClickListener(OnUserChatClickListener onUserChatClickListener) {
        this.onUserChatClickListener = onUserChatClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item_view,
                parent,
                false
        );
        return new UsersChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersChatsViewHolder holder, int position) {
        User user = users.get(position);
        holder.userNameBox.setText(
                String.format("%s %s", user.getName(), user.getLastName())
        );
        holder.userOtherInfoBox.setText(String.valueOf(user.getAge()));
        holder.messageCntIndicator.setText("");
        setOnlineStatus(holder.onlineStatusBox, user.isOnlineStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserChatClickListener != null){
                    onUserChatClickListener.onUserChatClick(user);
                }
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

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserChatClickListener{
        void onUserChatClick(User user);
    }

    static class UsersChatsViewHolder extends RecyclerView.ViewHolder{
        private TextView userNameBox;
        private TextView userOtherInfoBox;
        private TextView messageCntIndicator;
        private View onlineStatusBox;

        public UsersChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameBox = itemView.findViewById(R.id.userNameBox);
            userOtherInfoBox = itemView.findViewById(R.id.userOtherInfoBox);
            messageCntIndicator = itemView.findViewById(R.id.messageCntIndicator);
            onlineStatusBox = itemView.findViewById(R.id.onlineStatusBox);
        }
    }
}
