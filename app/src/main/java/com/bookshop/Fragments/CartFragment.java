package com.bookshop.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookshop.Adapter.CartAdapter;
import com.bookshop.Helper.ChangeNumberItemsListener;
import com.bookshop.Helper.ManagementCart;
import com.bookshop.databinding.FragmentCartBinding;

public class CartFragment extends Fragment {

    private ManagementCart managementCart;
    private FragmentCartBinding binding;
    private double tax;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        managementCart = new ManagementCart(getContext());

        initList();
        calculatorCart();

        return rootView;
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round(managementCart.getTotalFee() * percentTax * 100) / 100;

        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;
        binding.totalFeeTxt.setText("Ksh" + itemTotal);
        binding.taxTxt.setText("Ksh" + tax);
        binding.deliveryTxt.setText("Ksh" + delivery);
        binding.totalTxt.setText("Ksh" + total);
    }

    private void initList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scroll.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scroll.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(), new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculatorCart();
            }
        }, getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
