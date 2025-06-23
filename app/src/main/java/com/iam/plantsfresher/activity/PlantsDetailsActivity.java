package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.manager.CartManager;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.List;

public class PlantsDetailsActivity extends AppCompatActivity {

    public static String Tag = "PlantsDetailsActivity";
    ImageView imgPlant, btnBack,btnShare;
    TextView tvPlantName, tvPlantCategory, tvDescription, tvOffPrice,tvRealPrice, tvRatingValue;
    RatingBar ratingBarAverage;
    Button btnAddToCart,btnWishlist;
    LinearLayout layoutCareInstructions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plants_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initViews();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.setClassLoader(PlantsModel.class.getClassLoader());  // Set ClassLoader explicitly
            PlantsModel plant = extras.getParcelable("plant");

            if (plant != null) {
                bindDataToViews(plant);
                btnShare.setOnClickListener(v -> sharePlants(plant));

            } else {
                Log.e(Tag, "Plant object is null");
                finish();  // Close activity if data is missing
            }
        } else {
            Log.e(Tag, "No extras found in intent");
            finish();
        }

        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());


        // working of add to cart button
        btnAddToCart.setOnClickListener(v -> {
            PlantsModel plant = getIntent().getParcelableExtra("plant");
            if (plant != null) {
                CartManager.getInstance().addToCart(plant, 1);
                Toast.makeText(this, plant.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            }

        });

// Add this to handle wishlist button
//        btnWishlist.setOnClickListener(v -> {
//            // Toggle wishlist state
//            boolean isWishlisted = false; // You'll need to implement your wishlist logic
//            if (isWishlisted) {
//                btnWishlist.setIconResource(R.drawable.ic_wishlist_outlined);
//                Toast.makeText(this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
//            } else {
//                btnWishlist.setIconResource(R.drawable.ic_wishlist_filled);
//                Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void initViews() {
        imgPlant = findViewById(R.id.imgPlant);
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnWishlist = findViewById(R.id.btnWishlist);
        tvPlantName = findViewById(R.id.tvPlantName);
        tvPlantCategory = findViewById(R.id.tvPlantCategory);
        tvDescription = findViewById(R.id.tvDescription);
        tvOffPrice = findViewById(R.id.tvOffPrice);
        tvRealPrice = findViewById(R.id.tvRealPrice);
        tvRatingValue = findViewById(R.id.tvRatingValue);
        ratingBarAverage = findViewById(R.id.ratingBarAverage);
        layoutCareInstructions = findViewById(R.id.layoutCareInstructions);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void bindDataToViews(PlantsModel plant) {
        try {
            Glide.with(this)
                    .load(plant.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imgPlant);

            // Set basic plant info
            tvPlantName.setText(plant.getName());
            tvPlantCategory.setText(plant.getCategory());
            tvDescription.setText(plant.getDescription());

            // Set prices
            tvOffPrice.setText(String.format("$%.2f", plant.getOfferedPrice()));
            tvRealPrice.setText(String.format("$%.2f", plant.getRealPrice()));
            tvRealPrice.setPaintFlags(tvRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            btnAddToCart.setText(String.format("Add to Cart - $%.2f", plant.getOfferedPrice()));

            // Care instructions would go here if you implement them
            List<String> careInstructions = plant.getCareInstruction();

            LinearLayout layoutCareInstructions = findViewById(R.id.layoutCareInstructions);
            layoutCareInstructions.removeAllViews(); // remove previous views

            LayoutInflater inflater = LayoutInflater.from(this);

            if (careInstructions != null && !careInstructions.isEmpty()) {
                for (String instruction : careInstructions) {
                    View itemView = inflater.inflate(R.layout.item_care_instruction, layoutCareInstructions, false);

                    TextView tvInstruction = itemView.findViewById(R.id.tvCareInstructions);
                    tvInstruction.setText(instruction); // text set here
                    layoutCareInstructions.addView(itemView);
                }
            }

            // Rating - consider adding these to your model
            ratingBarAverage.setRating(4.2f);
            tvRatingValue.setText("4.5");

        } catch (Exception e) {
            Log.e(Tag, "Error binding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void  sharePlants(PlantsModel plant){
        if (plant != null) {
            String shareText = "🌿 *" + plant.getName() + "*\n\n"
                    + plant.getDescription() + "\n\n"
                    + "💲Price: $" + String.format("%.2f", plant.getRealPrice()) + "\n\n"
                    + "🛒 Download our app to buy more plants!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this plant!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            // Open share dialog
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else {
            Toast.makeText(this, "Plant data not available", Toast.LENGTH_SHORT).show();
        }
    }
}