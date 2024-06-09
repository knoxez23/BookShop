package com.bookshop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookshop.Activity.MainActivity;
import com.bookshop.Adapter.WishlistAdapter;
import com.bookshop.Domain.PopularDomain;
import com.bookshop.Helper.TinyDB;
import com.bookshop.databinding.FragmentWishlistBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding binding;
    private TinyDB tinyDB;
    private List<PopularDomain> wishlistItems;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        tinyDB = new TinyDB(requireContext());
        wishlistItems = tinyDB.getListObject("wishlist");

        MainActivity activity = (MainActivity) getActivity() ;
        assert activity != null;
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Wishlist");

        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        // Set up RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.WishlistView.setLayoutManager(gridLayoutManager);
        WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistItems, tinyDB);
        binding.WishlistView.setAdapter(wishlistAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
