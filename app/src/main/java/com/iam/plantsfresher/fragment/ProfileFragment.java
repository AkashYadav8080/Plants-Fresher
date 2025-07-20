package com.iam.plantsfresher.fragment;

import static com.iam.plantsfresher.activity.SignInActivity.KEY_EMAIL;
import static com.iam.plantsfresher.activity.SignInActivity.KEY_PASSWORD;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.activity.MainActivity;
import com.iam.plantsfresher.activity.SignInActivity;
import com.iam.plantsfresher.activity.SignUpActivity;


public class ProfileFragment extends Fragment {

    private LinearLayout loginBeforeLayout, loginAfterLayout;
    private Button btnLogin, btnEditProfile, btnLogout;
    private TextView txtSignUp, tvUserName, tvUserEmail, tvUserPhone, tvUserAddress;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER = "remember";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, 0);

        // Initialize views
        initializeViews(view);

        // Set click listeners
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        loginBeforeLayout = view.findViewById(R.id.loginBeforeLayout);
        loginAfterLayout = view.findViewById(R.id.loginAfterLayout);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        txtSignUp = view.findViewById(R.id.txtSignUp);

        // Profile views
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        tvUserPhone = view.findViewById(R.id.tv_user_phone);
        tvUserAddress = view.findViewById(R.id.tv_user_address);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> navigateToSignIn());
        txtSignUp.setOnClickListener(v -> navigateToSignUp());
        btnLogout.setOnClickListener(v -> logoutUser());
        btnEditProfile.setOnClickListener(v -> showEditProfile());
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

//    private void updateUI() {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
//
//        if (currentUser != null && rememberMe) {
//            // User is logged in
//            loginBeforeLayout.setVisibility(View.GONE);
//            loginAfterLayout.setVisibility(View.VISIBLE);
//            fetchUserData(currentUser.getUid());
//        } else {
//            // User is not logged in
//            loginBeforeLayout.setVisibility(View.VISIBLE);
//            loginAfterLayout.setVisibility(View.GONE);
//        }
//    }

    private void updateUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Only check Firebase auth state, not rememberMe
        if (currentUser != null) {
            // User is logged in (regardless of rememberMe)
            loginBeforeLayout.setVisibility(View.GONE);
            loginAfterLayout.setVisibility(View.VISIBLE);
            fetchUserData(currentUser.getUid());
        } else {
            // User is not logged in
            loginBeforeLayout.setVisibility(View.VISIBLE);
            loginAfterLayout.setVisibility(View.GONE);
        }
    }
    private void fetchUserData(String userId) {
        db.collection("PlantsFresher").document("Users").collection("Users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Populate user data
                            tvUserName.setText(document.getString("name"));
                            tvUserEmail.setText(document.getString("email"));

                            // Handle optional fields
                            if (document.contains("phone")) {
                                tvUserPhone.setText(document.getString("phone"));
                            } else {
                                tvUserPhone.setText("Not provided");
                            }

                            if (document.contains("address")) {
                                tvUserAddress.setText(document.getString("address"));
                            } else {
                                tvUserAddress.setText("Not provided");
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToSignIn() {
        startActivity(new Intent(getActivity(), SignInActivity.class));
    }

    private void navigateToSignUp() {
        startActivity(new Intent(getActivity(), SignUpActivity.class));
    }

    private void showEditProfile() {
        Toast.makeText(getContext(), "Edit profile feature coming soon", Toast.LENGTH_SHORT).show();
    }

//    private void logoutUser() {
//        mAuth.signOut();
//
//        // Clear remember me preference
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(KEY_REMEMBER, false);
//        editor.apply();
//
//        // Show logout message
//        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
//
//        // Refresh the activity to clear any cached data
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        requireActivity().finish();
//    }

    private void logoutUser() {
        mAuth.signOut();

        // Clear all login preferences on logout
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(KEY_REMEMBER, false); // Explicitly set rememberMe to false
        editor.apply();

        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}