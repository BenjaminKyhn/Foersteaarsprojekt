package com.example.android.androidapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Begivenhed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BegivenhedAdapter extends RecyclerView.Adapter<BegivenhedAdapter.BegivenhedHolder> {
    private ArrayList<Begivenhed> begivenheder = new ArrayList<>();

    @NonNull
    @Override
    public BegivenhedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.begivenhed_adapter_item, parent, false);
        return new BegivenhedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BegivenhedHolder holder, int position) {
        Begivenhed begivenhed = begivenheder.get(position);

        String titel = begivenhed.getTitel();

        Date date1 = new Date(begivenhed.getStartTidspunkt());
        Date date2 = new Date(begivenhed.getSlutTidspunkt());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String start = simpleDateFormat.format(date1);
        String slut = simpleDateFormat.format(date2);

        String concat = titel + ": " + start + " - " + slut;

        holder.textView.setText(concat);
    }

    @Override
    public int getItemCount() {
        return begivenheder.size();
    }

    public void setBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
        notifyDataSetChanged();
    }

    static class BegivenhedHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public BegivenhedHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.begivenhedTextView);
        }
    }
}
