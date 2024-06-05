package com.bookshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Domain.PopularDomain;
import com.bookshop.Helper.TinyDB;
import com.bookshop.R;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private final List<PopularDomain> wishlistItems;
    private final TinyDB tinyDB;

    public WishlistAdapter(List<PopularDomain> wishlistItems, TinyDB tinyDB) {
        this.wishlistItems = wishlistItems;
        this.tinyDB = tinyDB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularDomain item = wishlistItems.get(position);
        // Bind item data to ViewHolder views
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declare ViewHolder views here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize ViewHolder views here
        }
    }
}
