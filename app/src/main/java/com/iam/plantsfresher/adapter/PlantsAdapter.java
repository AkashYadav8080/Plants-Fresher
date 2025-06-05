package com.iam.plantsfresher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iam.plantsfresher.R;
import com.iam.plantsfresher.activity.PlantsDetailsActivity;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.List;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantViewHolder> {

    Context context;
    List<PlantsModel> plantsList;

    public PlantsAdapter(Context context, List<PlantsModel> plantsList) {
        this.context = context;
        this.plantsList = plantsList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plants, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantsModel plant = plantsList.get(position);

        holder.plantName.setText(plant.getName());
        holder.plantCategory.setText(plant.getCategory());
        holder.offPrice.setText(String.format("$%.2f", plant.getOfferedPrice()));

        // Load image using Glide
        Glide.with(context)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.splash)
                .into(holder.plantImage);

        // navigate views
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlantsDetailsActivity.class);
            intent.putExtra("plant", plant);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return plantsList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImage;
        TextView plantName, plantCategory, offPrice;


        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);

            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
            plantCategory = itemView.findViewById(R.id.plantCategory);
            offPrice = itemView.findViewById(R.id.offPrice);
        }
    }

    // Method to update the list
    public void updateList(List<PlantsModel> newList) {
        plantsList = newList;
        notifyDataSetChanged();
    }
}