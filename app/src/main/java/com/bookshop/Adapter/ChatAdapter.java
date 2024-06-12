package com.bookshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Models.ChatMessage;
import com.bookshop.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;
    private static final int VIEW_TYPE_DATE_TAG = 3;

    private List<ChatMessage> chatMessageList;
    private String currentUserId;

    public ChatAdapter(List<ChatMessage> chatMessageList, String currentUserId) {
        this.chatMessageList = chatMessageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = chatMessageList.get(position);
        if (chatMessage.isDateTag()) {
            return VIEW_TYPE_DATE_TAG;
        } else if (chatMessage.getSender().equals(currentUserId)) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_RECEIVER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_sender, parent, false);
            return new SenderViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_receiver, parent, false);
            return new ReceiverViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_date, parent, false);
            return new DateTagViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessageList.get(position);

        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder) holder).bind(chatMessage);
        } else if (holder instanceof ReceiverViewHolder) {
            ((ReceiverViewHolder) holder).bind(chatMessage);
        } else if (holder instanceof DateTagViewHolder) {
            ((DateTagViewHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text);
            timestampTextView = itemView.findViewById(R.id.timestamp);
        }

        public void bind(ChatMessage chatMessage) {
            messageTextView.setText(chatMessage.getMessage());
            timestampTextView.setText(formatTimestamp(chatMessage.getTimestamp()));
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text);
            timestampTextView = itemView.findViewById(R.id.timestamp);
        }

        public void bind(ChatMessage chatMessage) {
            messageTextView.setText(chatMessage.getMessage());
            timestampTextView.setText(formatTimestamp(chatMessage.getTimestamp()));
        }
    }

    public static class DateTagViewHolder extends RecyclerView.ViewHolder {
        TextView dateTagTextView;

        public DateTagViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTagTextView = itemView.findViewById(R.id.date_tag);
        }

        public void bind(ChatMessage chatMessage) {
            dateTagTextView.setText(formatDate(chatMessage.getTimestamp()));
        }
    }

    private static String formatTimestamp(Date timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(timestamp);
    }

    private static String formatDate(Date timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
