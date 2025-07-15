package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.iam.plantsfresher.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ImageButton backButton;
    private Button signUpButton;
    TextView txtLogin;
    private EditText edtName, edtEmail, edtPassword;
    private CheckBox rememberMe;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        signUpButton = findViewById(R.id.signUpButton);
        txtLogin = findViewById(R.id.txtLogin);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        rememberMe = findViewById(R.id.rememberMe);

        backButton.setOnClickListener(v -> onBackPressed());

        signUpButton.setOnClickListener(v -> {
            if (validateForm()) {
                createAccount();
            }
        });

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        // Name validation
        String name = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Full name is required");
            valid = false;
        } else {
            edtName.setError(null);
        }

        // Email validation
        String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please enter a valid email");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        // Password validation
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            valid = false;
        } else if (password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        // Terms checkbox validation
        if (!rememberMe.isChecked()) {
            Toast.makeText(this, "Please agree to our terms of use and privacy notice", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void createAccount() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String name = edtName.getText().toString().trim();

        // Show loading indicator (you can implement this)
        signUpButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Save login details if "Remember Me" is checked
                            if (rememberMe.isChecked()) {
                                saveLoginDetails(email, password);
                            }

                            // Save additional user data to Firestore
                            saveUserToFirestore(user.getUid(), name, email);
                        }
                    } else {
                        // If sign up fails, display a message to the user.
                        signUpButton.setEnabled(true);
                        Toast.makeText(SignUpActivity.this, "Authentication failed: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveLoginDetails(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, true);
        editor.apply();
    }

    private void saveUserToFirestore(String userId, String name, String email) {
        // Create a new user with additional fields
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("createdAt", System.currentTimeMillis());
        user.put("role", "user"); // You can add roles if needed

        // Add a new document with the user ID
        db.collection("PlantsFresher").document("Users").collection("Users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    signUpButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Send email verification
                        sendEmailVerification();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Registration successful! Please check your email for verification.",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Registration successful but failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}