package com.bookshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bookshop.Fragments.CartFragment;
import com.bookshop.Fragments.HomeFragment;
import com.bookshop.Fragments.ProfileFragment;
import com.bookshop.Fragments.WishlistFragment;
import com.bookshop.R;
import com.bookshop.databinding.ActivityMainBinding;
import com.bookshop.databinding.InfoPopupBinding;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SHOW_POPUP = "show_popup";
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check for the fragment extra in the Intent
        Intent intent = getIntent();
        String fragment = intent.getStringExtra("fragment");

        if ("cart".equals(fragment)) {
            replaceFragment(new CartFragment());
        } else {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNav.setBackground(null);
        getWindow().setStatusBarColor(getResources().getColor(R.color.cool_purple_light));
        bottomNavigation();

        if (shouldShowPopup()) {
            binding.getRoot().post(this::FileUploadPopup);
        }
    }

    private boolean shouldShowPopup() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_SHOW_POPUP, true);
    }

    private void setShowPopup(boolean show) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_SHOW_POPUP, show);
        editor.apply();
    }

    private void FileUploadPopup() {
        InfoPopupBinding infoPopupBinding = InfoPopupBinding.inflate(LayoutInflater.from(this));
        PopupWindow popupWindow = new PopupWindow(infoPopupBinding.getRoot(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_popup));

        infoPopupBinding.popupDismiss.setOnClickListener(v -> {
            getWindow().setStatusBarColor(getResources().getColor(R.color.cool_purple_light));
            popupWindow.dismiss();
        });

        infoPopupBinding.dontShowAgain.setOnClickListener( v -> {
            setShowPopup(false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.cool_purple_light));
            popupWindow.dismiss();
        });

        // Display the popup window in the center of the screen
        binding.getRoot().post(() -> {
            popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
        });

//        Display popup above the upload button
//        binding.uploadList.post( () -> {
//            // Position popup above button
//            int[] location = new int[2];
//            binding.uploadList.getLocationOnScreen(location);
//            int x = location[0] - (popupWindow.getWidth() - binding.uploadList.getWidth()) / 2;
//            int y = location[1] - popupWindow.getHeight();
//            popupWindow.showAtLocation(binding.uploadList, 0, x, y);
//        });
    }

    private void bottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener( item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.explorer) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.wishlist) {
                replaceFragment(new WishlistFragment());
            } else if (itemId == R.id.cart) {
                replaceFragment(new CartFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}