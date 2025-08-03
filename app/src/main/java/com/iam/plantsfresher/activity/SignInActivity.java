package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iam.plantsfresher.R;
import com.qdesk.toasty.Toaster;

public class SignInActivity extends AppCompatActivity {

    Button btnSkipLogin, loginButton;
    TextView txtForgotPassword, txtSignUp;
    EditText edtEmail, edtPassword;
    CheckBox rememberMe;

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize views
        btnSkipLogin = findViewById(R.id.btnSkipLogin);
        loginButton = findViewById(R.id.loginButton);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtSignUp = findViewById(R.id.txtSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        rememberMe = findViewById(R.id.rememberMe);

        // Check if we have saved login details
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            edtEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            edtPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            rememberMe.setChecked(true);
        }

        btnSkipLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                edtEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                edtPassword.setError("Password is required");
                return;
            }

            // Show loading indicator (you can implement this)
            loginButton.setEnabled(false);

            // Authenticate with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        loginButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            // Save login details if "Remember Me" is checked
                            if (rememberMe.isChecked()) {
                                saveLoginDetails(email, password);
                            } else {
//                                clearLoginDetails();
                                clearSavedCredentials();
                            }

                            // Check if email is verified
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {

                                Toaster.success(SignInActivity.this, "Login successful!");
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();
//                                new Handler().postDelayed(() -> {
//                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
//                                    finish();
//                                },2000);

                            } else {
//                                Toast.makeText(SignInActivity.this,
//                                        "Please verify your email address first.",
//                                        Toast.LENGTH_SHORT).show();
                                Toaster.warning(this, "Please verify your email address first.");
                                mAuth.signOut();
                            }
                        } else {
//                            Toast.makeText(SignInActivity.this,
//                                    "Authentication failed: " + task.getException().getMessage(),
//                                    Toast.LENGTH_SHORT).show();
                            Toaster.error(this, "Authentication failed: " + task.getException().getMessage());
                        }
                    });
        });

        txtForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgotPassActivity.class));
        });

        txtSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private void saveLoginDetails(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, true);
        editor.apply();
    }

    private void clearLoginDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(KEY_REMEMBER, false);
        editor.apply();
    }

    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        // Don't modify KEY_REMEMBER here
        editor.apply();
    }
}