package com.bookshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bookshop.R;
import com.bookshop.databinding.ActivityAdminSignupBinding;
import com.bookshop.databinding.ActivitySignupBinding;

public class AdminSignupActivity extends BaseActivity {
    ActivityAdminSignupBinding binding;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnClick();
    }

    private void setOnClick() {
        binding.signupBtn.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Creating");
            progressDialog.setMessage("Account");
            progressDialog.show();
            String email = binding.userEdt.getText().toString();
            String password = binding.passwordEdt.getText().toString();

            if(password.length() < 6) {
                Toast.makeText(AdminSignupActivity.this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(AdminSignupActivity.this, task -> {
                if(task.isSuccessful()) {
                    Log.i(TAG, "onComplete: ");
                    startActivity(new Intent(AdminSignupActivity.this, AdminMainActivity.class));
                    finish();
                } else {
                    Log.i(TAG, "failure: " + task.getException());
                    Toast.makeText(AdminSignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}