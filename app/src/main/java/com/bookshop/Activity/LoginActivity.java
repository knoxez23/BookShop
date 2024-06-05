package com.bookshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bookshop.R;
import com.bookshop.databinding.ActivityLoginBinding;
import com.bookshop.databinding.AdminAuthPopupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    private DatabaseReference mAuthReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuthReference = database.getReference("authentication");
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        setOnClick();
    }

    private void setOnClick() {
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passwordEdt.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
            }
        });

        binding.goToSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        binding.adminAuth.setOnClickListener(v -> {
            showAdminAuthPopup();
        });
    }

    private void showAdminAuthPopup() {
        AdminAuthPopupBinding popupBinding = AdminAuthPopupBinding.inflate(LayoutInflater.from(this));
        PopupWindow popupWindow = new PopupWindow(popupBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupBinding.cancelBtn.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        popupBinding.authBtn.setOnClickListener(v -> {
            String enteredKey = popupBinding.authKeyEdt.getText().toString().trim();
            authenticateAdminKey(enteredKey, popupWindow);
        });

        // Show the popup in the middle of the screen
        popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
    }

    private void authenticateAdminKey(String enteredKey, PopupWindow popupWindow) {
        // Create a final reference to the PopupWindow
        final PopupWindow finalPopupWindow = popupWindow;

        mAuthReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if dataSnapshot exists and has children
                if (dataSnapshot.exists()) {
                    String storedKey = dataSnapshot.child("key").getValue(String.class);
                    if (storedKey != null && storedKey.equals(enteredKey)) {
                        // Key is valid
                        finalPopupWindow.dismiss();
                        startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
                        finish();
                    } else {
                        // Key is invalid
                        Toast.makeText(LoginActivity.this, "Invalid key. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Key doesn't exist
                    Toast.makeText(LoginActivity.this, "Authentication key not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
