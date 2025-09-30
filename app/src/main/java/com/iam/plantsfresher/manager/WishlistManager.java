package com.iam.plantsfresher.manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iam.plantsfresher.model.PlantsModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WishlistManager {
    private static WishlistManager instance;
    private List<PlantsModel> wishlistItems;
    private Set<String> wishlistIds;
    private SharedPreferences prefs;
    private Gson gson;
    private static final String PREFS_NAME = "WishlistPrefs";
    private static final String KEY_WISHLIST = "wishlist_items";

    private WishlistManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        wishlistItems = new ArrayList<>();
        wishlistIds = new HashSet<>();
        loadWishlist();
    }

    public static synchronized WishlistManager getInstance(Context context) {
        if (instance == null) {
            instance = new WishlistManager(context);
        }
        return instance;
    }

    public static WishlistManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("WishlistManager must be initialized with context first");
        }
        return instance;
    }

    public void addToWishlist(PlantsModel plant) {
        if (!isInWishlist(plant.getId())) {
            wishlistItems.add(plant);
            wishlistIds.add(plant.getId());
            saveWishlist();
        }
    }

    public void removeFromWishlist(String plantId) {
        wishlistItems.removeIf(item -> item.getId().equals(plantId));
        wishlistIds.remove(plantId);
        saveWishlist();
    }

    public boolean isInWishlist(String plantId) {
        return wishlistIds.contains(plantId);
    }

    public List<PlantsModel> getWishlistItems() {
        return new ArrayList<>(wishlistItems);
    }

    public int getWishlistCount() {
        return wishlistItems.size();
    }

    public void clearWishlist() {
        wishlistItems.clear();
        wishlistIds.clear();
        saveWishlist();
    }

    private void saveWishlist() {
        String json = gson.toJson(wishlistItems);
        prefs.edit().putString(KEY_WISHLIST, json).apply();
    }

    private void loadWishlist() {
        String json = prefs.getString(KEY_WISHLIST, null);
        if (json != null) {
            Type type = new TypeToken<List<PlantsModel>>(){}.getType();
            wishlistItems = gson.fromJson(json, type);
            if (wishlistItems == null) {
                wishlistItems = new ArrayList<>();
            }
            // Rebuild the ID set
            wishlistIds.clear();
            for (PlantsModel plant : wishlistItems) {
                wishlistIds.add(plant.getId());
            }
        }
    }
}