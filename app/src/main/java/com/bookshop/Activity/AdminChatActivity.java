package com.bookshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bookshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminChatActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<String> userList;
    private List<String> userIds; // Add a list to store user IDs
    private ArrayAdapter<String> userAdapter;
    private String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db = FirebaseFirestore.getInstance();

        ListView userListView = findViewById(R.id.user_list_view);
        userList = new ArrayList<>();
        userIds = new ArrayList<>(); // Initialize the user IDs list
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        userListView.setAdapter(userAdapter);

        loadUsers();

        userListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUsername = userList.get(position);
            String selectedUserId = userIds.get(position); // Use the corresponding user ID
            Intent intent = new Intent(AdminChatActivity.this, ChatActivity.class);
            intent.putExtra("userId", selectedUserId);
            intent.putExtra("username", selectedUsername);
            startActivity(intent);
        });

    }

    private void loadUsers() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                userIds.clear(); // Clear the user IDs list
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String userId = doc.getId();
                    String username = doc.getString("username");
                    if (!userId.equals(currentUserId) && username != null) {
                        userList.add(username);
                        userIds.add(userId); // Add the user ID to the list
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }
}
