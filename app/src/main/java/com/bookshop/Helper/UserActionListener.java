package com.bookshop.Helper;

import com.bookshop.Models.UserModel;

public interface UserActionListener {
    void onViewDetails(UserModel userModel);
    void onDeleteUser(UserModel userModel);
}
