package com.bookshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookshop.Fragments.AdminOrders;
import com.bookshop.Fragments.AdminProducts;
import com.bookshop.Fragments.ChatFragment;
import com.bookshop.Fragments.HomeFragment;
import com.bookshop.Fragments.Users;
import com.bookshop.R;
import com.bookshop.databinding.ActivityAdminMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityAdminMainBinding binding;
//    private String id, title, category, description, price;
//    private Uri uri;
//    private ProgressBar progressBar;
    FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(getResources().getColor(R.color.cool_purple_light));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AdminProducts()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        fetchAndDisplayUserName();
        bottomNavigation();


//        progressBar = findViewById(R.id.progressBar);

//        binding.addProduct.setOnClickListener(v -> {
//            title = binding.title.getText().toString();
//            category = binding.category.getText().toString();
//            description = binding.description.getText().toString();
//            price = binding.price.getText().toString();
//            addProduct();
//        });
//
//        binding.image.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//            startActivityForResult(intent, 100);
//        });
//
//        binding.uploadPic.setOnClickListener(v -> uploadImage());
    }

    private void bottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.products) {
                replaceFragment(new AdminProducts());
            } else if (itemId == R.id.orders) {
                replaceFragment(new AdminOrders());
            } else if (itemId == R.id.users) {
                replaceFragment(new Users());
            } else if (itemId == R.id.chat) {
                startActivity(new Intent(this, AdminChatActivity.class));
            }
            return true;
        });
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
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

//    private void uploadImage() {
//        if (uri == null) {
//            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setProgress(0);
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("products/" + id + ".png");
//        UploadTask uploadTask = storageReference.putFile(uri);
//
//        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> FirebaseFirestore.getInstance()
//                .collection("products")
//                .document(id)
//                .update("image", uri.toString())
//                .addOnSuccessListener(aVoid -> {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(AdminMainActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(AdminMainActivity.this, "Failed to update Firestore", Toast.LENGTH_SHORT).show();
//                })).addOnFailureListener(e -> {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(AdminMainActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
//        })).addOnFailureListener(e -> {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(AdminMainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
//        }).addOnProgressListener(snapshot -> {
//            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//            progressBar.setProgress((int) progress);
//        });
//    }

//    private void addProduct() {
//        id = UUID.randomUUID().toString();
//        ProductModel productModel = new ProductModel(id, title, category, description, price, null, true);
//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .document(id)
//                .set(productModel);
//        Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//            uri = data.getData();
//            binding.image.setImageURI(uri);
//        }
//    }
}
