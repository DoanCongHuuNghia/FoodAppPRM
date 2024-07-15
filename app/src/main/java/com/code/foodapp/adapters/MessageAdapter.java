package com.code.foodapp.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private String currentUser;

    public MessageAdapter(List<Message> messageList, String currentUser) {
        this.messageList = messageList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message, currentUser);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView messageTextView;
        private TextView timeTextView;
        private LinearLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }

        public void bind(Message message, String currentUser) {
            senderTextView.setText(message.getSender());
            messageTextView.setText(message.getMessage());
            timeTextView.setText(message.getTime());

            // Align message based on the sender
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageContainer.getLayoutParams();
            if (message.getSender().equals(currentUser)) {
                params.gravity = Gravity.END;
                messageContainer.setBackgroundResource(R.drawable.message_background_sent);
            } else {
                params.gravity = Gravity.START;
                messageContainer.setBackgroundResource(R.drawable.message_background_received);
            }
            messageContainer.setLayoutParams(params);
        }
    }
}
