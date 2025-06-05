package com.iam.plantsfresher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.activity.MainActivity;
import com.iam.plantsfresher.adapter.PlantsAdapter;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView plantsRecycler;
    private PlantsAdapter plantsAdapter;
    private List<PlantsModel> plantsModelList = new ArrayList<>();
    private List<PlantsModel> allPlantsList = new ArrayList<>();
    private ExtendedFloatingActionButton fabScrollToTop;
    private boolean isBottomNavVisible = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        plantsRecycler = view.findViewById(R.id.plantsRecycler);
        fabScrollToTop = view.findViewById(R.id.fabScrollToTop);

        // Initialize plants data
        initializePlantsData();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup chip group
        setupChipGroup(view);

        // Setup scroll listener
        setupScrollListener(view);

        // Setup scroll to top button
        setupScrollToTopButton();

        return view;
    }

    private void initializePlantsData() {
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

        plantsModelList.addAll(allPlantsList);
    }

    private void setupRecyclerView() {
        plantsAdapter = new PlantsAdapter(getContext(), plantsModelList);
        plantsRecycler.setAdapter(plantsAdapter);
        plantsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setupChipGroup(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == View.NO_ID) {
                return;
            }

            String selectedCategory = "";
            Chip chip = view.findViewById(checkedId);
            if (chip != null) {
                selectedCategory = chip.getText().toString();
            }

            filterPlantsByCategory(selectedCategory);
        });

        // Select "All" chip by default
        Chip chipAll = view.findViewById(R.id.chipAll);
        if (chipAll != null) {
            chipAll.setChecked(true);
        }
    }

    private void filterPlantsByCategory(String category) {
        plantsModelList.clear();

        if (category.equals("All")) {
            plantsModelList.addAll(allPlantsList);
        } else {
            for (PlantsModel plant : allPlantsList) {
                if (plant.getCategory().equals(category)) {
                    plantsModelList.add(plant);
                }
            }
        }

        plantsAdapter.notifyDataSetChanged();

        if (plantsModelList.isEmpty()) {
            Toast.makeText(getContext(), "No plants found in this category", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupScrollListener(View view) {
        view.findViewById(R.id.scrollView).getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = view.findViewById(R.id.scrollView).getScrollY();

            // Show/hide bottom nav based on scroll direction
            if (scrollY > 100) { // Threshold to start hiding
                if (isBottomNavVisible) {
                    isBottomNavVisible = false;
                }
            } else {
                if (!isBottomNavVisible) {
                    isBottomNavVisible = true;
                }
            }

            // Show/hide scroll to top button
            if (scrollY > 500) {
                fabScrollToTop.show();
            } else {
                fabScrollToTop.hide();
            }
        });
    }

    private void setupScrollToTopButton() {
        fabScrollToTop.setOnClickListener(v -> {
            NestedScrollView scrollView = getView().findViewById(R.id.scrollView);
            if (scrollView != null) {
                scrollView.smoothScrollTo(0, 0);
            }
        });
    }
}