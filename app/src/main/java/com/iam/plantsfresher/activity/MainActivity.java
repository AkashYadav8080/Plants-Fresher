package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.fragment.ShoppingFragment;
import com.iam.plantsfresher.fragment.HomeFragment;
import com.iam.plantsfresher.fragment.ProfileFragment;
import com.iam.plantsfresher.fragment.WishlistFragment;
import com.onesignal.Continue;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    ImageView imgMenu;
    DrawerLayout drawerLayout;
    int currentBottomNavItemId = R.id.bottom_home; // Track only bottom nav items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use this method to prompt for push notifications.
        // We recommend removing this method after testing and instead use In-App Messages to prompt for notification permission.
        OneSignal.getNotifications().requestPermission(false, Continue.none());

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView = findViewById(R.id.navigationView);
        imgMenu = findViewById(R.id.imgMenu);
        drawerLayout = findViewById(R.id.drawerLayout);

        // Cart button
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(v -> {
            // Handle cart click
        });

        // Drawer toggle
        imgMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottom_home);
            currentBottomNavItemId = R.id.bottom_home;
        }

        // Bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            currentBottomNavItemId = item.getItemId(); // Update current bottom nav item
            return handleNavigation(item.getItemId());
        });

        // Side navigation
        navigationView.setNavigationItemSelectedListener(item -> handleNavigation(item.getItemId()));


        // Navigate to cart fragment if intent extra is "cart"
        Intent intent = getIntent();
        if (intent != null && "cart".equals(intent.getStringExtra("navigateTo"))) {
            // Replace with your actual fragment transaction code
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment_host, new ShoppingFragment())
                    .commit();
            // Highlight cart in bottom navigation if you have one
            bottomNavigationView.setSelectedItemId(R.id.navigationView);
        }

    }

    private boolean handleNavigation(int itemId) {
        Fragment selectedFragment = null;

        // Close drawer if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (itemId == R.id.bottom_home || itemId == R.id.side_home) {
            selectedFragment = new HomeFragment();
            if (itemId == R.id.side_home) {
                bottomNavigationView.setSelectedItemId(R.id.bottom_home);
                currentBottomNavItemId = R.id.bottom_home;
            }
        } else if (itemId == R.id.bottom_wishlist || itemId == R.id.side_wishlist) {
            selectedFragment = new WishlistFragment();
            if (itemId == R.id.side_wishlist) {
                bottomNavigationView.setSelectedItemId(R.id.bottom_wishlist);
                currentBottomNavItemId = R.id.bottom_wishlist;
            }
        } else if (itemId == R.id.bottom_hang || itemId == R.id.side_hang) {
            selectedFragment = new ShoppingFragment();
            if (itemId == R.id.side_hang) {
                bottomNavigationView.setSelectedItemId(R.id.bottom_hang);
                currentBottomNavItemId = R.id.bottom_hang;
            }
        } else if (itemId == R.id.bottom_profile || itemId == R.id.side_profile) {
            selectedFragment = new ProfileFragment();
            if (itemId == R.id.side_profile) {
                bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
                currentBottomNavItemId = R.id.bottom_profile;
            }
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                )
                .replace(R.id.nav_fragment_host, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (currentBottomNavItemId != R.id.bottom_home) {
            // Navigate to home and update bottom navigation
            bottomNavigationView.setSelectedItemId(R.id.bottom_home);
            currentBottomNavItemId = R.id.bottom_home;
            loadFragment(new HomeFragment());
        } else {
            super.onBackPressed();
        }
    }
}