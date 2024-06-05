package com.bookshop.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.bookshop.databinding.ActivityAdminMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AdminMainActivity extends AppCompatActivity {
    ActivityAdminMainBinding binding;
    private String id, title, category, description, price;
    private Uri uri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);

        binding.addProduct.setOnClickListener(v -> {
            title = binding.title.getText().toString();
            category = binding.category.getText().toString();
            description = binding.description.getText().toString();
            price = binding.price.getText().toString();
            addProduct();
        });

        binding.image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        binding.uploadPic.setOnClickListener(v -> uploadImage());
    }

    private void uploadImage() {
        if (uri == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("products/" + id + ".png");
        UploadTask uploadTask = storageReference.putFile(uri);

        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> FirebaseFirestore.getInstance()
                .collection("products")
                .document(id)
                .update("image", uri.toString())
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminMainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminMainActivity.this, "Failed to update Firestore", Toast.LENGTH_SHORT).show();
                })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AdminMainActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AdminMainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            progressBar.setProgress((int) progress);
        });
    }

    private void addProduct() {
        id = UUID.randomUUID().toString();
        ProductModel productModel = new ProductModel(id, title, category, description, price, null, true);
        FirebaseFirestore.getInstance()
                .collection("products")
                .document(id)
                .set(productModel);
        Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            binding.image.setImageURI(uri);
        }
    }
}
