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

public class SignInActivity extends AppCompatActivity {

    Button loginButton;
    TextView txtForgotPassword,txtSignUp;

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


//      find views
        loginButton = findViewById(R.id.loginButton);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtSignUp = findViewById(R.id.txtSignUp);

        loginButton.setOnClickListener(v -> {
            Intent a = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(a);
        });

        txtForgotPassword.setOnClickListener(v -> {
            Intent a = new Intent(SignInActivity.this, ForgotPassActivity.class);
            startActivity(a);
        });

        txtSignUp.setOnClickListener(v -> {
            Intent a = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(a);
        });


    }
}