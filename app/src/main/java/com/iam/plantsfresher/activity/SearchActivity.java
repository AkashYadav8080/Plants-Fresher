package com.iam.plantsfresher.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iam.plantsfresher.R;
import com.iam.plantsfresher.adapter.PlantsAdapter;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText txtSearch;
    private RecyclerView recyclerViewSearch;
    private PlantsAdapter plantsAdapter;
    private List<PlantsModel> allPlantsList = new ArrayList<>();
    private List<PlantsModel> filteredPlantsList = new ArrayList<>();
    private LinearLayout tvNoResults;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initializePlantsData();
        setupRecyclerView();
        setupSearchFunctionality();
        setupClickListeners();
    }

    private void initViews() {
        txtSearch = findViewById(R.id.txtSearch);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        tvNoResults = findViewById(R.id.tvNoResults);
        btnBack = findViewById(R.id.btnBack);

        // Request focus and show keyboard
        txtSearch.requestFocus();
    }

    private void initializePlantsData() {
        // Initialize the same data as in HomeFragment
        allPlantsList.clear();

        allPlantsList.add(new PlantsModel(
                "P1", "Aloe Vera", "Recommend",
                "Aloe vera is a natural plant.",
                Arrays.asList("Bright, indirect light", "Water when top soil is dry"),
                33.00, 30.00, 1,
                "https://extension.sdstate.edu/sites/default/files/2024-04/W-01884-02-Houseplant-How-To-Aloe-Vera.jpg"
        ));

        allPlantsList.add(new PlantsModel(
                "P2", "Snake Plant", "Top",
                "Also known as mother-in-law's tongue. Extremely hardy.",
                Arrays.asList("Low to bright light", "Water every 2-3 weeks"),
                59.99, 45.00, 1,
                "https://m.media-amazon.com/images/I/51IE6ForlbL._AC_UF1000,1000_QL80_.jpg"
        ));

        allPlantsList.add(new PlantsModel(
                "P3", "Peace Lily", "Indoor",
                "Peace lilies are air-purifying and thrive indoors.",
                Arrays.asList("Low to medium light", "Keep soil moist"),
                45.50, 39.99, 1,
                "https://www.ugaoo.com/cdn/shop/products/Growpot_e29e91e2-5021-4cca-817a-0b8f898ff73b.jpg"
        ));

        allPlantsList.add(new PlantsModel(
                "P4", "Areca Palm", "Outdoor",
                "Areca palms grow well in shaded outdoor spaces.",
                Arrays.asList("Filtered sunlight", "Water when soil is dry"),
                120.00, 99.00, 1,
                "https://nurserylive.com/cdn/shop/products/nurserylive-g-areca-palm-small-plant.jpg"
        ));

        allPlantsList.add(new PlantsModel(
                "P5", "Cactus", "Drought Plants",
                "Cactus is a low-maintenance drought-tolerant plant.",
                Arrays.asList("Full sun", "Water once every 3 weeks"),
                25.00, 19.99, 20,
                "https://5.imimg.com/data5/VB/AS/MY-2679819/colourful-flower-cactus-plants-500x500.jpg"
        ));

        allPlantsList.add(new PlantsModel(
                "P6", "Iris versicolor", "Wetland Plants",
                "Also known as blue flag iris, it thrives in wetlands.",
                Arrays.asList("Full sun to partial shade", "Prefers consistently moist to wet soil"),
                35.00, 29.50, 1,
                "https://nurserylive.com/cdn/shop/collections/nurserylive-aromatic-fragrant-plants-category-image-380980_600x600.jpg"
        ));

        // Initially show all plants
        filteredPlantsList.addAll(allPlantsList);
    }

    private void setupRecyclerView() {
        plantsAdapter = new PlantsAdapter(this, filteredPlantsList);
        recyclerViewSearch.setAdapter(plantsAdapter);
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setupSearchFunctionality() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlants(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filterPlants(String searchQuery) {
        filteredPlantsList.clear();

        if (searchQuery.isEmpty()) {
            // Show all plants if search is empty
            filteredPlantsList.addAll(allPlantsList);
        } else {
            // Filter plants based on search query
            String query = searchQuery.toLowerCase();

            for (PlantsModel plant : allPlantsList) {
                // Search in plant name
                if (plant.getName().toLowerCase().contains(query)) {
                    filteredPlantsList.add(plant);
                    continue;
                }

                // Search in category
                if (plant.getCategory().toLowerCase().contains(query)) {
                    filteredPlantsList.add(plant);
                    continue;
                }

                // Search in description
                if (plant.getDescription().toLowerCase().contains(query)) {
                    filteredPlantsList.add(plant);
                    continue;
                }


            }
        }

        // Update UI based on search results
        updateSearchResults();
    }

    private void updateSearchResults() {
        if (filteredPlantsList.isEmpty() && !txtSearch.getText().toString().trim().isEmpty()) {
            recyclerViewSearch.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
//            tvNoResults.setText("No plants found for \"" + txtSearch.getText().toString().trim() + "\"");
        } else {
            // Show results
            recyclerViewSearch.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }

        plantsAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        ImageView micIcon = findViewById(R.id.micIcon);
        if (micIcon != null) {
            micIcon.setOnClickListener(v -> {
                Toast.makeText(this, "Voice search coming soon!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}