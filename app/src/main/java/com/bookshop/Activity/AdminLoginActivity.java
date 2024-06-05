package com.bookshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bookshop.R;
import com.bookshop.databinding.ActivityAdminLoginBinding;
import com.bookshop.databinding.ActivityLoginBinding;
import com.google.firebase.database.DatabaseReference;

public class AdminLoginActivity extends BaseActivity {
    ActivityAdminLoginBinding binding;

    private DatabaseReference mAuthReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
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
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AdminLoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(AdminLoginActivity.this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
            }
        });

        binding.goToSignup.setOnClickListener(v -> {
            startActivity(new Intent(AdminLoginActivity.this, SignupActivity.class));
        });

    }
}