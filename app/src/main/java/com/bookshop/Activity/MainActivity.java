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

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bookshop.Fragments.CartFragment;
import com.bookshop.Fragments.HomeFragment;
import com.bookshop.Fragments.WishlistFragment;
import com.bookshop.R;
import com.bookshop.databinding.ActivityMainBinding;
import com.bookshop.databinding.InfoPopupBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SHOW_POPUP = "show_popup";

    private DrawerLayout drawerLayout;
    ActivityMainBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fetchAndDisplayUserName();

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
        bottomSheetDialog();

        if (shouldShowPopup()) {
            binding.getRoot().post(this::FileUploadPopup);
        }

    }

    private void bottomSheetDialog() {
        View upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }

            private void showBottomDialog() {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_sheet_layout);

                LinearLayout uploadList = dialog.findViewById(R.id.uploadList);
                LinearLayout createList = dialog.findViewById(R.id.createList);
                LinearLayout makeCall = dialog.findViewById(R.id.makeCall);
                ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

                uploadList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

                    }
                });

                createList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

                    }
                });

                makeCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
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

    private void fetchAndDisplayUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName;

        if (user != null) {
            userName = user.getDisplayName(); // Fetch and display name
            if (userName == null || userName.isEmpty()) {
                userName = user.getEmail(); // If display name is not set, fall back to email
            }
        } else {
            userName = "Guest"; // If user is not logged in, set to "Guest"
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView loggedInUserTextView = headerView.findViewById(R.id.loggedInUser);
        loggedInUserTextView.setText(userName);
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
            } else if (itemId == R.id.chat) {
                fetchAndPassUserId();
            }
            return true;
        });
    }

    private void fetchAndPassUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String userId = task.getResult().getString("id");
                    if (userId != null) {
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch user ID", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
