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

import com.bookshop.Adapter.AdminProductsAdapter;
import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminProducts extends Fragment {

    private FirebaseFirestore db;
    private AdminProductsAdapter productsAdapter;
    private List<ProductModel> productModelList;

    public AdminProducts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        RecyclerView productsRecyclerView = view.findViewById(R.id.admin_products_recycler_view);
        productModelList = new ArrayList<>();
        productsAdapter = new AdminProductsAdapter(getContext(), productModelList, new AdminProductsAdapter.ProductActionListener() {
            @Override
            public void onToggleShow(ProductModel productModel) {
                toggleProductVisibility(productModel);
            }

            @Override
            public void onDelete(ProductModel productModel) {
                deleteProduct(productModel);
            }

            @Override
            public void onEdit(ProductModel productModel) {
                // Implement edit product functionality
                Toast.makeText(getContext(), "Edit functionality not implemented", Toast.LENGTH_SHORT).show();
            }
        });

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productsRecyclerView.setAdapter(productsAdapter);

        loadProducts();
    }

    private void loadProducts() {
        db.collection("products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productModelList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    ProductModel productModel = doc.toObject(ProductModel.class);
                    productModel.setId(doc.getId());
                    productModelList.add(productModel);
                }
                productsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleProductVisibility(ProductModel productModel) {
        db.collection("products").document(productModel.getId())
                .update("show", productModel.isShow())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Product visibility updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update product visibility", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteProduct(ProductModel productModel) {
        db.collection("products").document(productModel.getId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                productModelList.remove(productModel);
                productsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String category) {
        List<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel product : productModelList) {
            if (category.equals("All") || product.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }
        productsAdapter.updateList(filteredList);
    }
}
