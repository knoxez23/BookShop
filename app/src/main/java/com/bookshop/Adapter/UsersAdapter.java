package com.bookshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookshop.Helper.UserActionListener;
import com.bookshop.Models.UserModel;
import com.bookshop.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context context;
    private List<UserModel> userModelList;
    private UserActionListener actionListener;

    public UsersAdapter(Context context, List<UserModel> userModelList, UserActionListener actionListener) {
        this.context = context;
        this.userModelList = userModelList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.MyViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);
        holder.name.setText(userModel.getUsername());

        holder.itemView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.inflate(R.menu.user_options_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.view_details) {
                    actionListener.onViewDetails(userModel);
                    return true;
                } else if (item.getItemId() == R.id.delete_user) {
                    actionListener.onDeleteUser(userModel);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
        }
    }
}
