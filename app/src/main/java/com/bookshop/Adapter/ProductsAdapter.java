package com.bookshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Activity.DetailActivity;
import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private final Context context;
    private final List<ProductModel> productModelList;

    public ProductsAdapter(Context context) {
        this.context = context;
        productModelList = new ArrayList<>();
    }

    public void addProduct(ProductModel productModel) {
        productModelList.add(productModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.MyViewHolder holder, int position) {
        ProductModel productModel = productModelList.get(position);
        if (productModel != null) {
            Log.d("ProductsAdapter", "Title: " + productModel.getTitle());
            Log.d("ProductsAdapter", "Description: " + productModel.getDescription());
            Log.d("ProductsAdapter", "Price: " + productModel.getPrice());

            holder.title.setText(productModel.getTitle());
            holder.desc.setText(productModel.getDescription());
            holder.price.setText(productModel.getPrice());

            Glide.with(context).load(productModel.getImage()).into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("model", productModel);
                    context.startActivity(intent);
                }
            });
        } else {
            Log.e("ProductsAdapter", "ProductModel is null at position: " + position);
        }
    }


    public  int getItemCount() {
        return productModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView desc;
        private final TextView price;
        private final ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.image);
        }
    }
}
