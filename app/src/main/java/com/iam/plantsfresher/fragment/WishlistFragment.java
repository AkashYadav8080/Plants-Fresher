package com.iam.plantsfresher.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iam.plantsfresher.R;
import com.iam.plantsfresher.adapter.WishlistAdapter;
import com.iam.plantsfresher.manager.WishlistManager;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.List;

public class WishlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private WishlistAdapter adapter;
    private LinearLayout tvEmptyWishlist;
    private WishlistManager wishlistManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        wishlistManager = WishlistManager.getInstance(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewWishlist);
        tvEmptyWishlist = view.findViewById(R.id.tvEmptyWishlist);

        setupRecyclerView();
        loadWishlistItems();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new WishlistAdapter(getContext(), wishlistManager.getWishlistItems(),
                this::onWishlistItemRemoved);
        recyclerView.setAdapter(adapter);
    }

    private void loadWishlistItems() {
        List<PlantsModel> wishlistItems = wishlistManager.getWishlistItems();

        if (wishlistItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyWishlist.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyWishlist.setVisibility(View.GONE);
            adapter.updateList(wishlistItems);
        }
    }

    private void onWishlistItemRemoved() {
        loadWishlistItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWishlistItems();
    }
}