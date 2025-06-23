package com.iam.plantsfresher.manager;

import com.iam.plantsfresher.model.CartItem;
import com.iam.plantsfresher.model.PlantsModel;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(PlantsModel plant, int quantity) {
        // Check if the plant is already in the cart
        for (CartItem item : cartItems) {
            if (item.getPlant().getId().equals(plant.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If not, add as new item
        cartItems.add(new CartItem(plant, quantity));
    }

    public void removeFromCart(String plantId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getPlant().getId().equals(plantId)) {
                cartItems.remove(i);
                return;
            }
        }
    }

    public void updateQuantity(String plantId, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getPlant().getId().equals(plantId)) {
                if (newQuantity <= 0) {
                    removeFromCart(plantId);
                } else {
                    item.setQuantity(newQuantity);
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy to prevent external modification
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}