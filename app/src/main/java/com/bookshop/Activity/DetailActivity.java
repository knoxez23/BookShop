package com.bookshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bookshop.Domain.PopularDomain;
import com.bookshop.Helper.ManagementCart;
import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.bookshop.databinding.ActivityDetailBinding;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private PopularDomain object;
    private final int numberOrder = 1;
    private ManagementCart managementCart;
//    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        ProductModel productModel = (ProductModel) intent.getSerializableExtra("model");

        assert productModel != null;
        binding.titleTxt.setText(productModel.getTitle());
        binding.descriptionTxt.setText(productModel.getDescription());
        binding.priceTxt.setText(productModel.getPrice());
        Glide.with(binding.getRoot())
                        .load(productModel.getImage())
                        .into(binding.itemPic);
        binding.backBtn.setOnClickListener(v -> finish());
//        getBundles();
        managementCart = new ManagementCart(this);

        binding.addToCartBtn.setOnClickListener(v -> {
//            showProgressBar();
            PopularDomain item = new PopularDomain(
                    productModel.getTitle(),
                    productModel.getImage(),
                    0,
                    0,
                    Double.parseDouble(productModel.getPrice()),
                    productModel.getDescription()
            );
            item.setNumberInCart(numberOrder);
            managementCart.insertFood(item);
//            handler.postDelayed(() -> {
//
//                hideProgressBar();
//            }, 2000);
        });
        statusBarColor();
    }

//    private void hideProgressBar() {
//        binding.progressBarDetail.setVisibility(View.GONE);
//        binding.addToCartBtn.setEnabled(true);
//    }
//
//    private void showProgressBar() {
//        binding.progressBarDetail.setVisibility(View.VISIBLE);
//        binding.addToCartBtn.setEnabled(false);
//    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.white));
    }

}