package com.bookshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bookshop.Adapter.ProductsAdapter;
import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.bookshop.databinding.ActivityProductBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.cool_purple_light));
        productsAdapter = new ProductsAdapter(this);
        binding.productRecycler.setAdapter(productsAdapter);
        binding.productRecycler.setLayoutManager(new LinearLayoutManager(this));

        binding.backBtn.setOnClickListener(v -> finish());

        binding.cart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, MainActivity.class);
            intent.putExtra("fragment", "cart");
            startActivity(intent);
            finish();
        });
        
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("searchText")) {
                String searchText = intent.getStringExtra("searchText");
                if (searchText != null && !searchText.isEmpty()) {
                    performSearch(searchText);
                } else {
                    // Default behavior: Get all products
                    getProducts();
                }
            } else if (intent.hasExtra("category")) {
                String category = intent.getStringExtra("category");
                if (category != null) {
                    getProductsByCategory(category);
                }
            } else {
                // Default behavior: Get all products
                getProducts();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void performSearch(String searchText) {
        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("show", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot ds : dsList) {
                        ProductModel productModel = ds.toObject(ProductModel.class);
                        assert productModel != null;
                        if (productModelContainsSearchText(productModel, searchText)) {
                            // Add the matching product to the adapter
                            productsAdapter.addProduct(productModel);
                        }
                    }

                    // Display the number of search results found
                    Toast.makeText(ProductActivity.this, productsAdapter.getItemCount() + " results found", Toast.LENGTH_SHORT).show();
                    binding.progressBarProducts.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    binding.progressBarProducts.setVisibility(View.GONE);
                    // Handle the error here, possibly show a message to the user
                    Toast.makeText(ProductActivity.this, "Failed to retrieve, please try again later", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean productModelContainsSearchText(ProductModel productModel, String searchText) {
        // Check each field in the product model for the searchText
        // Add more fields if needed
        // productModel.getFieldName().toLowerCase().contains(searchText.toLowerCase()) ||
        // productModel.getFieldName().toLowerCase().contains(searchText.toLowerCase()) ||
        // ...
        return productModel.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                productModel.getDescription().toLowerCase().contains(searchText.toLowerCase());
    }

    private void getProductsByCategory(String category) {
        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("show", true)
                .whereEqualTo("category", category)  // Filter by category
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot ds : dsList) {
                        ProductModel productModel = ds.toObject(ProductModel.class);
                        productsAdapter.addProduct(productModel);
                    }
                    binding.progressBarProducts.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    binding.progressBarProducts.setVisibility(View.GONE);
                    // Handle the error here, possibly show a message to the user
                    Toast.makeText(ProductActivity.this, "Failed to retrieve, please try again later", Toast.LENGTH_SHORT).show();
                });
    }

    private void getProducts() {
//        binding.progressBarProducts.setVisibility(View.VISIBLE);
//        binding.productRecycler.setVisibility(View.GONE);

        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("show", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot ds : dsList) {
                        ProductModel productModel = ds.toObject(ProductModel.class);
                        productsAdapter.addProduct(productModel);
                    }
                    binding.progressBarProducts.setVisibility(View.GONE);
//                    binding.productRecycler.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    binding.progressBarProducts.setVisibility(View.GONE);
                    // Handle the error here, possibly show a message to the user
                    Toast.makeText(ProductActivity.this, "Failed to retrieve, please try again later", Toast.LENGTH_SHORT).show();
                });
    }

}
