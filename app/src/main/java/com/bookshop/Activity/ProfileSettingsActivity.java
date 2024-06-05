package com.bookshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bookshop.R;
import com.bookshop.databinding.ActivityProfileSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileSettingsActivity extends AppCompatActivity {
    ActivityProfileSettingsBinding binding;
    FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityProfileSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=  FirebaseAuth.getInstance();
        statusBarColor();

        binding.backBtn.setOnClickListener( v -> finish());
        binding.logOutBtn.setOnClickListener( v -> logoutUser());
    }

    private void logoutUser() {
        if (mAuth != null) {
            mAuth.signOut();
            Toast.makeText(ProfileSettingsActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(ProfileSettingsActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(ProfileSettingsActivity.this, "Error: Unable to logout", Toast.LENGTH_SHORT).show();
        }
    }

    private void statusBarColor() {
        Window window = ProfileSettingsActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ProfileSettingsActivity.this, R.color.white));
    }
}
