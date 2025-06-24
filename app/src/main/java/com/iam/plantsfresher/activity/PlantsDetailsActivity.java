package com.iam.plantsfresher.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.manager.CartManager;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlantsDetailsActivity extends AppCompatActivity {

    public static String Tag = "PlantsDetailsActivity";
    ImageView imgPlant, btnBack,btnShare;
    TextView tvPlantName, tvPlantCategory, tvDescription, tvOffPrice,tvRealPrice, tvRatingValue,cartItemCount;
    RatingBar ratingBarAverage;
    Button btnAddToCart,btnWishlist;
    LinearLayout layoutCareInstructions;

    CardView btnViewCart;
    CircleImageView cartItemImage;
    private boolean isCartViewVisible = false;

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

            // Update the view cart card
            updateViewCartCard(plant);

            // Show the view cart card with animation
            showViewCartCard();
        });

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

        btnViewCart = findViewById(R.id.btnViewCart);
        cartItemImage = findViewById(R.id.cart_item_image);
        cartItemCount = findViewById(R.id.cart_item_count);

        // Set click listener for view cart button
        btnViewCart.setOnClickListener(v -> {
            // Navigate to cart fragment
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigateTo", "cart");
            startActivity(intent);
            hideViewCartCard();
        });

//        ScrollView scrollView = findViewById(R.id.main);
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
//            int scrollY = scrollView.getScrollY();
//            if (scrollY > 100) { // If scrolled down more than 100px
//                hideViewCartCard();
//            } else {
//                showViewCartCard();
//            }
//        });
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

    // update view cart
    private void updateViewCartCard(PlantsModel plant) {
        // Load plant image
        Glide.with(this)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(cartItemImage);

        // Update item count
        int cartItemCount = CartManager.getInstance().getCartItems().size();
        String itemText = cartItemCount == 1 ? "1 ITEM" : cartItemCount + " ITEMS";
        this.cartItemCount.setText(itemText);
    }

    private void showViewCartCard() {
        if (!isCartViewVisible) {
            isCartViewVisible = true;
            btnViewCart.setVisibility(View.VISIBLE);

            // Set initial position below the screen
            btnViewCart.setTranslationY(btnViewCart.getHeight());

            // Animate up
            btnViewCart.animate()
                    .translationY(0)
                    .setDuration(500)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }
    private void hideViewCartCard() {
        if (isCartViewVisible) {
            isCartViewVisible = false;
            btnViewCart.animate()
                    .translationY(btnViewCart.getHeight())
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> btnViewCart.setVisibility(View.GONE))
                    .start();
        }
    }

    // Add this to prevent hiding when clicking on the card itself
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCartViewVisible) {
            Rect viewRect = new Rect();
            btnViewCart.getGlobalVisibleRect(viewRect);
            if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                hideViewCartCard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}