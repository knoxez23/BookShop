package com.bookshop.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Helper.UserActionListener;
import com.bookshop.Adapter.UsersAdapter;
import com.bookshop.Models.UserModel;
import com.bookshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Users extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private List<UserModel> userModelList;
    private String currentUserId;

    public Users() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        userModelList = new ArrayList<>();
        usersAdapter = new UsersAdapter(getContext(), userModelList, new UserActionListener() {
            @Override
            public void onViewDetails(UserModel userModel) {
                showUserDetails(userModel);
            }

            @Override
            public void onDeleteUser(UserModel userModel) {
                deleteUser(userModel);
            }
        });

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersRecyclerView.setAdapter(usersAdapter);

        loadUsers();
    }

    private void loadUsers() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userModelList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String userId = doc.getId();
                    if (!userId.equals(currentUserId)) {
                        UserModel userModel = doc.toObject(UserModel.class);
                        userModel.setId(userId);
                        userModelList.add(userModel);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserDetails(UserModel userModel) {
        // Implement functionality to show user details
        Toast.makeText(getContext(), "User: " + userModel.getUsername(), Toast.LENGTH_SHORT).show();
    }

    private void deleteUser(UserModel userModel) {
        db.collection("users").document(userModel.getId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                userModelList.remove(userModel);
                usersAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
