package com.bookshop.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bookshop.Activity.MainActivity;
import com.bookshop.Activity.ProductActivity;
import com.bookshop.Adapter.ProductsAdapter;
import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.bookshop.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    private ProductsAdapter productsAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Enable menu options in toolbar
        setHasOptionsMenu(true);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Mel's Bookshop");

        // Set up the OnClickListener for the search EditText
//        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                performSearch(binding.searchEditText.getText().toString().trim());
//                return true;
//            }
//            return false;
//        });

        binding.goToProducts.setOnClickListener(v -> startActivity(new Intent(requireContext(), ProductActivity.class)));
        binding.goToProductsBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), ProductActivity.class)));
        binding.openProducts.setOnClickListener(v -> startActivity(new Intent(requireContext(), ProductActivity.class)));

        binding.booksCat.setOnClickListener(v -> navigateToProductActivity("Books"));
        binding.penPencilCat.setOnClickListener(v -> navigateToProductActivity("Pen/Pencils"));
        binding.rulersCat.setOnClickListener(v -> navigateToProductActivity("Rulers"));
        binding.erasersCat.setOnClickListener(v -> navigateToProductActivity("Erasers"));

        // Fetch and display the user name
        fetchAndDisplayUserName();
        initRecyclerView();
        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_view, menu);
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }



    private void navigateToProductActivity(String category) {
        Intent intent = new Intent(requireContext(), ProductActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void fetchAndDisplayUserName() {
        // Fetch and display the user name if needed
    }

    private void initRecyclerView() {
        if (binding == null) {
            // Return or handle appropriately if binding or PopularView is null
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        productsAdapter = new ProductsAdapter(getContext()); // Initialize the adapter with the context

        db.collection("products")
                .whereEqualTo("show", true)
                .limit(5) // Limit to 5 items
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot ds : dsList) {
                        ProductModel productModel = ds.toObject(ProductModel.class);
                        productsAdapter.addProduct(productModel);
                    }

                    // Set the adapter for RecyclerView
                    binding.PopularView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    binding.PopularView.setAdapter(productsAdapter);

                    // Hide the progress bar after data retrieval
                    binding.progressBarPopular.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                    Log.e("FirestoreError", "Error getting documents: ", e);

                    // Hide the progress bar in case of failure
                    binding.progressBarPopular.setVisibility(View.GONE);
                });
    }
}
