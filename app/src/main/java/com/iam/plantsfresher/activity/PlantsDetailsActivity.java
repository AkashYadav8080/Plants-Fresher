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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.manager.CartManager;
import com.iam.plantsfresher.manager.WishlistManager;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlantsDetailsActivity extends AppCompatActivity {

    public static String Tag = "PlantsDetailsActivity";
    ImageView imgPlant, btnBack, btnShare;
    TextView tvPlantName, tvPlantCategory, tvDescription, tvOffPrice, tvRealPrice, tvRatingValue, cartItemCount;
    RatingBar ratingBarAverage;
    Button btnAddToCart;
    MaterialButton btnWishlist;
    LinearLayout layoutCareInstructions;

    CardView btnViewCart;
    CircleImageView cartItemImage;
    private boolean isCartViewVisible = false;

    private PlantsModel currentPlant;
    private WishlistManager wishlistManager;

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

        // Initialize wishlist manager
        wishlistManager = WishlistManager.getInstance(this);

        // Initialize views
        initViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.setClassLoader(PlantsModel.class.getClassLoader());
            currentPlant = extras.getParcelable("plant");

            if (currentPlant != null) {
                bindDataToViews(currentPlant);
                updateWishlistButton();
                btnShare.setOnClickListener(v -> sharePlants(currentPlant));
            } else {
                Log.e(Tag, "Plant object is null");
                finish();
            }
        } else {
            Log.e(Tag, "No extras found in intent");
            finish();
        }

        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());

        // Wishlist button click
        btnWishlist.setOnClickListener(v -> toggleWishlist());

        // Add to cart button
        btnAddToCart.setOnClickListener(v -> {
            if (currentPlant != null) {
                CartManager.getInstance().addToCart(currentPlant, 1);
                Toast.makeText(this, currentPlant.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                updateViewCartCard(currentPlant);
                showViewCartCard();
            }
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

        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigateTo", "cart");
            startActivity(intent);
            hideViewCartCard();
        });
    }

    private void toggleWishlist() {
        if (currentPlant == null) return;

        if (wishlistManager.isInWishlist(currentPlant.getId())) {
            wishlistManager.removeFromWishlist(currentPlant.getId());
            Toast.makeText(this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
        } else {
            wishlistManager.addToWishlist(currentPlant);
            Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show();
        }
        updateWishlistButton();
    }

    private void updateWishlistButton() {
        if (currentPlant != null && wishlistManager.isInWishlist(currentPlant.getId())) {
            btnWishlist.setIconResource(R.drawable.ic_wishlist_filled);
        } else {
            btnWishlist.setIconResource(R.drawable.ic_wishlist_outlined);
        }
    }


    private void bindDataToViews(PlantsModel plant) {
        try {
            Glide.with(this)
                    .load(plant.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imgPlant);

            tvPlantName.setText(plant.getName());
            tvPlantCategory.setText(plant.getCategory());
            tvDescription.setText(plant.getDescription());

            tvOffPrice.setText(String.format("$%.2f", plant.getOfferedPrice()));
            tvRealPrice.setText(String.format("$%.2f", plant.getRealPrice()));
            tvRealPrice.setPaintFlags(tvRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            btnAddToCart.setText(String.format("Add to Cart - $%.2f", plant.getOfferedPrice()));

            List<String> careInstructions = plant.getCareInstruction();
            layoutCareInstructions.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            if (careInstructions != null && !careInstructions.isEmpty()) {
                for (String instruction : careInstructions) {
                    View itemView = inflater.inflate(R.layout.item_care_instruction, layoutCareInstructions, false);
                    TextView tvInstruction = itemView.findViewById(R.id.tvCareInstructions);
                    tvInstruction.setText(instruction);
                    layoutCareInstructions.addView(itemView);
                }
            }

            ratingBarAverage.setRating(4.2f);
            tvRatingValue.setText("4.5");

        } catch (Exception e) {
            Log.e(Tag, "Error binding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void sharePlants(PlantsModel plant) {
        if (plant != null) {
            String shareText = "🌿 *" + plant.getName() + "*\n\n"
                    + plant.getDescription() + "\n\n"
                    + "💲Price: $" + String.format("%.2f", plant.getRealPrice()) + "\n\n"
                    + "🛒 Download our app to buy more plants!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this plant!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else {
            Toast.makeText(this, "Plant data not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateViewCartCard(PlantsModel plant) {
        Glide.with(this)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(cartItemImage);

        int cartItemCount = CartManager.getInstance().getCartItems().size();
        String itemText = cartItemCount == 1 ? "1 ITEM" : cartItemCount + " ITEMS";
        this.cartItemCount.setText(itemText);
    }

    private void showViewCartCard() {
        if (!isCartViewVisible) {
            isCartViewVisible = true;
            btnViewCart.setVisibility(View.VISIBLE);
            btnViewCart.setTranslationY(btnViewCart.getHeight());
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