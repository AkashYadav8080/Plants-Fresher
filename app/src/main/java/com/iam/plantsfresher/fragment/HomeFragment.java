package com.iam.plantsfresher.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iam.plantsfresher.R;
import com.iam.plantsfresher.adapter.PlantsAdapter;
import com.iam.plantsfresher.model.PlantsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {


    RecyclerView plantsRecycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        plantsRecycler = view.findViewById(R.id.plantsRecycler);


        List<PlantsModel> plantsModelList = new ArrayList<>();

        plantsModelList.add(new PlantsModel(
                "P1",
                "Aloe Vera",
                "Recommend",
                "Aloe vera is a natural plant",
                Arrays.asList("Bright, indirect light", "Water when top soil is dry"),
                33.00,
                30.00,
                "https://extension.sdstate.edu/sites/default/files/2024-04/W-01884-02-Houseplant-How-To-Aloe-Vera.jpg"
        ));


        PlantsAdapter plantsAdapter = new PlantsAdapter(getContext(),plantsModelList);

        plantsRecycler.setAdapter(plantsAdapter);
        plantsRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));



        return view;
    }
}