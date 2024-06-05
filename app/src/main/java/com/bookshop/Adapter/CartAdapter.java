package com.bookshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Domain.PopularDomain;
import com.bookshop.Helper.ChangeNumberItemsListener;
import com.bookshop.Helper.ManagementCart;
import com.bookshop.databinding.ViewholderCartBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private final ArrayList<PopularDomain> items;
    private final Context context;
    private final ChangeNumberItemsListener changeNumberItemsListener;
    private final ManagementCart managementCart;

    public CartAdapter(ArrayList<PopularDomain> items, ChangeNumberItemsListener changeNumberItemsListener, Context context) {
        this.items = items;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.context = context;
        this.managementCart = new ManagementCart(context);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(inflater, parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        PopularDomain currentItem = items.get(position);

        holder.binding.titleTxt.setText(currentItem.getTitle());
        holder.binding.feeEachItem.setText("Ksh" + currentItem.getPrice());
        holder.binding.totalEachItem.setText("Ksh" + Math.round(currentItem.getNumberInCart() * currentItem.getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(currentItem.getNumberInCart()));

        Glide.with(context)
                .load(currentItem.getPicUrl())
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(v -> {
            managementCart.plusNumberItem(items, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });
        });

        holder.binding.minusCartBtn.setOnClickListener(v -> {
            managementCart.minusNumberItem(items, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
