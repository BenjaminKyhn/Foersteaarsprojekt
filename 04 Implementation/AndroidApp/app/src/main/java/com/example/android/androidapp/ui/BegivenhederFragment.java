package com.example.android.androidapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Begivenhed;

import java.util.ArrayList;

public class BegivenhederFragment extends Fragment {
    ArrayList<Begivenhed> begivenheder;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_begivenheder, container, false);
        recyclerView = view.findViewById(R.id.begivenhedRecyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BegivenhedAdapter begivenhedAdapter = new BegivenhedAdapter();
        begivenhedAdapter.setBegivenheder(begivenheder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(begivenhedAdapter);
    }

    public void angivBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
    }

}