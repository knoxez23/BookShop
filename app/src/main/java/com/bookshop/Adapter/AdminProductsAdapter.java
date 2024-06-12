package com.bookshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Models.ProductModel;
import com.bookshop.R;
import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.MyViewHolder> {

    private Context context;
    private List<ProductModel> productModelList;
    private ProductActionListener productActionListener;

    public AdminProductsAdapter(Context context, List<ProductModel> productModelList, ProductActionListener productActionListener) {
        this.context = context;
        this.productModelList = productModelList;
        this.productActionListener = productActionListener;
    }

    @NonNull
    @Override
    public AdminProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductsAdapter.MyViewHolder holder, int position) {
        ProductModel productModel = productModelList.get(position);
        holder.title.setText(productModel.getTitle());
        holder.category.setText(productModel.getCategory());
        holder.price.setText(productModel.getPrice());
        // Picasso.get().load(productModel.getImage()).into(holder.image);
        // Load image using Glide
        Glide.with(context)
                .load(productModel.getImage())
                .into(holder.image);
        holder.showToggle.setOnClickListener(v -> {
            // Toggle product visibility
            productModel.setShow(!productModel.isShow());
            productActionListener.onToggleShow(productModel);
            notifyItemChanged(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Delete product
            productActionListener.onDelete(productModel);
        });

        holder.editButton.setOnClickListener(v -> {
            // Edit product
            productActionListener.onEdit(productModel);
        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public void updateList(List<ProductModel> newList) {
        productModelList = newList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, category, price;
        private ImageView image;
        private ImageButton showToggle, deleteButton, editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_title);
            category = itemView.findViewById(R.id.product_category);
            price = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.product_image);
            showToggle = itemView.findViewById(R.id.product_show_toggle);
            deleteButton = itemView.findViewById(R.id.product_delete_button);
            editButton = itemView.findViewById(R.id.product_edit_button);
        }
    }

    public interface ProductActionListener {
        void onToggleShow(ProductModel productModel);
        void onDelete(ProductModel productModel);
        void onEdit(ProductModel productModel);
    }
}
