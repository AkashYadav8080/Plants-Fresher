package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iam.plantsfresher.R;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if user is already logged in
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);

            if (currentUser != null && rememberMe) {
                // User is logged in and "Remember Me" was checked - go to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // User is not logged in or didn't check "Remember Me" - go to SignInActivity
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }
            finish();
        }, 1000); // 1 second delay
    }
}