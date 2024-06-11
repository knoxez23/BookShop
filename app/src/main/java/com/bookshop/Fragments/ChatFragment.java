package com.bookshop.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookshop.Activity.MainActivity;
import com.bookshop.Activity.ProfileSettingsActivity;
import com.bookshop.Adapter.PopularAdapter;
import com.bookshop.Domain.PopularDomain;
import com.bookshop.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.Objects;


public class ChatFragment extends Fragment {

    FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Chat");


        return rootView;
    }



    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}