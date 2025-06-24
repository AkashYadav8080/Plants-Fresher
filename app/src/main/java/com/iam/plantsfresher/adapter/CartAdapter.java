package com.iam.plantsfresher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.manager.CartManager;
import com.iam.plantsfresher.model.CartItem;
import com.iam.plantsfresher.model.PlantsModel;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    private List<CartItem> cartItems;
    CartManager cartManager;
    CartUpdateListener cartUpdateListener;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartManager = CartManager.getInstance();

        if (context instanceof CartUpdateListener) {
            this.cartUpdateListener = (CartUpdateListener) context;
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        PlantsModel plant = cartItem.getPlant();

        holder.plantName.setText(plant.getName());
        holder.plantDesc.setText(plant.getDescription());
        holder.plantPrice.setText(String.format("$%.2f", cartItem.getTotalPrice()));
        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.plantImage);


        holder.btnMinus.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() - 1;

            if (newQuantity <= 0) {
                cartManager.removeItem(plant.getId());
                cartItems.remove(position); // remove from local list
                Toast.makeText(context, plant.getName()+" removed from cart", Toast.LENGTH_SHORT).show();
            } else {
                cartManager.updateQuantity(plant.getId(), newQuantity);
                cartItem.setQuantity(newQuantity); // update local item
            }

            notifyDataSetChanged();

            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }

        });


        holder.btnPlus.setOnClickListener(v -> {
            int currentQuantity = cartItem.getQuantity();

            if (currentQuantity >= 11) {
                Toast.makeText(context, "Maximum stock limit reached", Toast.LENGTH_SHORT).show();
                return;
            }

            int newQuantity = currentQuantity + 1;
            cartManager.updateQuantity(plant.getId(), newQuantity);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImage;
        TextView plantName, plantDesc, plantPrice, quantityText;
        ImageButton btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
            plantDesc = itemView.findViewById(R.id.plantDesc);
            plantPrice = itemView.findViewById(R.id.plantPrice);
            quantityText = itemView.findViewById(R.id.tvQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }
}