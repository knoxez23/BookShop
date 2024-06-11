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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference chatRef;
    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;
    private String userId; // Add userId variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userId = getIntent().getStringExtra("userId"); // Retrieve userId from intent

        db = FirebaseFirestore.getInstance();
        chatRef = db.collection("chats").document(userId).collection("messages");

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);

        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        loadMessages();
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Map<String, Object> chatMessage = new HashMap<>();
        chatMessage.put("message", message);
        chatMessage.put("sender", "admin"); // Or use actual admin ID

        chatRef.add(chatMessage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageInput.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        chatRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                return;
            }

            chatMessageList.clear();
            for (QueryDocumentSnapshot doc : value) {
                ChatMessage chatMessage = doc.toObject(ChatMessage.class);
                chatMessageList.add(chatMessage);
            }
            chatAdapter.notifyDataSetChanged();
        });
    }

}
