package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iam.plantsfresher.R;

public class SignUpActivity extends AppCompatActivity {

    ImageButton backButton;
    Button signUpButton;
    TextView txtLogin;

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

        backButton = findViewById(R.id.backButton);
        signUpButton = findViewById(R.id.signUpButton);
        txtLogin = findViewById(R.id.txtLogin);


        backButton.setOnClickListener(v -> {
           onBackPressed();
        });

        signUpButton.setOnClickListener(v -> {
            Intent a = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(a);
        });

        txtLogin.setOnClickListener(v -> {
            Intent a = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(a);
        });

    }
}