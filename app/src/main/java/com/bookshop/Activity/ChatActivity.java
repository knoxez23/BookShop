package com.bookshop.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bookshop.Adapter.ChatAdapter;
import com.bookshop.Models.ChatMessage;
import com.bookshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference chatRef;
    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;
    private String currentUserId;
    private String chatUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatUserId = getIntent().getStringExtra("userId");

        db = FirebaseFirestore.getInstance();
        chatRef = db.collection("chats").document(chatUserId).collection("messages");

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);

        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList, currentUserId);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> sendMessage());

        loadMessages();
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Map<String, Object> chatMessage = new HashMap<>();
        chatMessage.put("message", message);
        chatMessage.put("sender", currentUserId);
        chatMessage.put("timestamp", new Date());

        chatRef.add(chatMessage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageInput.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        chatRef.orderBy("timestamp").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                return;
            }

            chatMessageList.clear();
            Date previousDate = null;

            for (QueryDocumentSnapshot doc : value) {
                ChatMessage chatMessage = doc.toObject(ChatMessage.class);
                Date messageDate = chatMessage.getTimestamp();

                if (previousDate == null || !isSameDay(previousDate, messageDate)) {
                    ChatMessage dateTagMessage = new ChatMessage();
                    dateTagMessage.setTimestamp(messageDate);
                    dateTagMessage.setDateTag(true);
                    chatMessageList.add(dateTagMessage);
                }

                chatMessageList.add(chatMessage);
                previousDate = messageDate;
            }

            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(chatMessageList.size() - 1);
        });
    }

    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return sdf.format(date1).equals(sdf.format(date2));
    }
}
