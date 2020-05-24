package com.example.android.androidapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.domain.Besked;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VaelgChatHolder> {
    private List<Besked> beskeder = new ArrayList<>();


    @NonNull
    @Override
    public VaelgChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_besvar_besked, parent, false);
        return new VaelgChatHolder(view);
    }


    @Override
    public void onBindViewHolder(VaelgChatHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beskeder.size();
    }

    void setBeskeder(List<Besked> beskeder) {
        this.beskeder = beskeder;
        notifyDataSetChanged();
    }

    static class VaelgChatHolder extends RecyclerView.ViewHolder {

        VaelgChatHolder(View itemView) {
            super(itemView);
        }
    }
}
