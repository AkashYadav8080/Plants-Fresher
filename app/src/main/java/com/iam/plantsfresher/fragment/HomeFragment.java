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

        plantsModelList.add(new PlantsModel("P1","Plant","Recommend",33.33,4,"https://www.paudhewale.com/s/660a356584d1ac2391ae69de/663e24ed0a34072ed684c807/paudhewale-1-.png"));
        plantsModelList.add(new PlantsModel("P1","Plant","Top",233.33,4,"https://www.paudhewale.com/s/660a356584d1ac2391ae69de/663e24ed0a34072ed684c807/paudhewale-1-.png"));
        plantsModelList.add(new PlantsModel("P1","Plant","Indoor",73.00,5,"https://www.paudhewale.com/s/660a356584d1ac2391ae69de/663e24ed0a34072ed684c807/paudhewale-1-.png"));
        plantsModelList.add(new PlantsModel("P1","Plant","Outdoor",23.5,4,"https://www.paudhewale.com/s/660a356584d1ac2391ae69de/663e24ed0a34072ed684c807/paudhewale-1-.png"));
        plantsModelList.add(new PlantsModel("P1","Plant","Dry",23.33,5,"https://www.paudhewale.com/s/660a356584d1ac2391ae69de/663e24ed0a34072ed684c807/paudhewale-1-.png"));
        plantsModelList.add(new PlantsModel("P1","Plant","Water",2.33,4,"https://www.paudhewale.com/s/660a356584d1ac2391ae69de/663e24ed0a34072ed684c807/paudhewale-1-.png"));
//        plantsModelList.add(new PlantsModel("P1","Plant","Dry",233.33,4,""));
//        plantsModelList.add(new PlantsModel("P1","Plant","water",233.33,4,""));


        PlantsAdapter plantsAdapter = new PlantsAdapter(getContext(),plantsModelList);

        plantsRecycler.setAdapter(plantsAdapter);
        plantsRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));



        return view;
    }
}