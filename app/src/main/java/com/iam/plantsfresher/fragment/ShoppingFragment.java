package com.iam.plantsfresher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iam.plantsfresher.R;
import com.iam.plantsfresher.adapter.CartAdapter;
import com.iam.plantsfresher.manager.CartManager;
import com.iam.plantsfresher.model.CartItem;

import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView cartRecycler;
    private TextView emptyCartText;
    private CartAdapter cartAdapter;
    private CartManager cartManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        cartManager = CartManager.getInstance();
        cartRecycler = view.findViewById(R.id.cartRecycler);
        emptyCartText = view.findViewById(R.id.emptyCartText); // Add this TextView to your layout

        setupRecyclerView();
        updateCartView();


        return view;
    }

    private void setupRecyclerView() {
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), cartManager.getCartItems());
        cartRecycler.setAdapter(cartAdapter);
    }

    private void updateCartView() {
        List<CartItem> cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            cartRecycler.setVisibility(View.GONE);
            emptyCartText.setVisibility(View.VISIBLE);
        } else {
            cartRecycler.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.GONE);
            cartAdapter.updateCartItems(cartItems);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartView();
    }
}