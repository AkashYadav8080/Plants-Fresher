package com.iam.plantsfresher.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.activity.SignInActivity;
import com.iam.plantsfresher.activity.SignUpActivity;

public class ProfileFragment extends Fragment {

    LinearLayout loginBeforeLayout,loginAfterLayout;
    Button btnLogin,btnEditProfile,btnLogout;
    TextView txtSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        loginBeforeLayout = view.findViewById(R.id.loginBeforeLayout);
        loginAfterLayout = view.findViewById(R.id.loginAfterLayout);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        txtSignUp = view.findViewById(R.id.txtSignUp);


        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
        });

        txtSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
        });




        return view;
    }
}