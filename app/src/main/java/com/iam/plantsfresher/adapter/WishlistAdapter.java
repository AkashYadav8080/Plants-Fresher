package com.iam.plantsfresher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.activity.PlantsDetailsActivity;
import com.iam.plantsfresher.manager.WishlistManager;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private Context context;
    private List<PlantsModel> wishlistItems;
    private OnWishlistChangedListener listener;

    public interface OnWishlistChangedListener {
        void onWishlistItemRemoved();
    }

    public WishlistAdapter(Context context, List<PlantsModel> wishlistItems,
                           OnWishlistChangedListener listener) {
        this.context = context;
        this.wishlistItems = wishlistItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        PlantsModel plant = wishlistItems.get(position);

        holder.plantName.setText(plant.getName());
        holder.plantCategory.setText(plant.getCategory());
        holder.offPrice.setText(String.format("$%.2f", plant.getOfferedPrice()));

        Glide.with(context)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.plantImage);

        // Navigate to details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlantsDetailsActivity.class);
            intent.putExtra("plant", plant);
            context.startActivity(intent);
        });

        // Remove from wishlist
        holder.btnRemoveWishlist.setOnClickListener(v -> {
            WishlistManager.getInstance().removeFromWishlist(plant.getId());
            Toast.makeText(context, plant.getName() + " removed from wishlist",
                    Toast.LENGTH_SHORT).show();
            wishlistItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, wishlistItems.size());

            if (listener != null) {
                listener.onWishlistItemRemoved();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public void updateList(List<PlantsModel> newList) {
        wishlistItems = newList;
        notifyDataSetChanged();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImage, btnRemoveWishlist;
        TextView plantName, plantCategory, offPrice;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
            plantCategory = itemView.findViewById(R.id.plantCategory);
            offPrice = itemView.findViewById(R.id.offPrice);
            btnRemoveWishlist = itemView.findViewById(R.id.btnRemoveWishlist);
        }
    }
}