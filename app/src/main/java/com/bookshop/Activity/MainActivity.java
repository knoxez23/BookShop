package com.bookshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bookshop.Fragments.CartFragment;
import com.bookshop.Fragments.HomeFragment;
import com.bookshop.Fragments.ChatFragment;
import com.bookshop.Fragments.WishlistFragment;
import com.bookshop.R;
import com.bookshop.databinding.ActivityMainBinding;
import com.bookshop.databinding.InfoPopupBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SHOW_POPUP = "show_popup";

    private DrawerLayout drawerLayout;
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);

        mAuth=  FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Set up search functionality
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when submit button is pressed
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as the query text changes
                // Optionally, you can implement live search here
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_bell) {
            Toast.makeText(this, "Bell icon clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void performSearch(String searchText) {
        // Start the ProductActivity and pass the search text as an extra
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("searchText", searchText);
        startActivity(intent);
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

        infoPopupBinding.dontShowAgain.setOnClickListener(v -> {
            setShowPopup(false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.cool_purple_light));
            popupWindow.dismiss();
        });

        binding.getRoot().post(() -> {
            popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
        });
    }

    private void bottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.explorer) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.wishlist) {
                replaceFragment(new WishlistFragment());
            } else if (itemId == R.id.cart) {
                replaceFragment(new CartFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ChatFragment());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            replaceFragment(new HomeFragment());
        } else if (itemId == R.id.nav_settings) {
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_share) {
            Toast.makeText(this, "Share Clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_about) {
            Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_logout) {
            logoutUser();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        if (mAuth != null) {
            mAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Error: Unable to logout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
