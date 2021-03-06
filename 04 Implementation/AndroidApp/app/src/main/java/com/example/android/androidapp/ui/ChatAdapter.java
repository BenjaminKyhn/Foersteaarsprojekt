package com.example.android.androidapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.model.BrugerFacade;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**@author Tommy**/
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VaelgChatHolder> {
    private ArrayList<Besked> beskeder = new ArrayList<>();

    @NonNull
    @Override
    public VaelgChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_item, parent, false);
        return new VaelgChatHolder(view);
    }


    @Override
    public void onBindViewHolder(VaelgChatHolder holder, int position) {
        Besked besked = beskeder.get(position);
        holder.afsender.setText(besked.getAfsender());
        holder.besked.setText(besked.getBesked());
        Date date = new Date(besked.getTidspunkt());
        String format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
        holder.tidspunkt.setText(format);
        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        Bruger bruger = brugerFacade.hentBrugerMedNavn(besked.getAfsender());
        if (bruger != null){
            String pictureUrl = brugerFacade.hentBrugerMedNavn(besked.getAfsender()).getFotoURL();
            Picasso.get().load(pictureUrl).into(holder.billede);
        }
        //TODO Virker kun for Camilla?
    }

    @Override
    public int getItemCount() {
        return beskeder.size();
    }

    void setBeskeder(ArrayList<Besked> beskeder) {
        this.beskeder = beskeder;
        notifyDataSetChanged();
    }

    static class VaelgChatHolder extends RecyclerView.ViewHolder {
        TextView afsender;
        TextView besked;
        TextView tidspunkt;
        ImageView billede;

        VaelgChatHolder(View itemView) {
            super(itemView);
            afsender = itemView.findViewById(R.id.textViewNavn);
            besked = itemView.findViewById(R.id.textViewBesked);
            tidspunkt = itemView.findViewById(R.id.textViewTidspunkt);
            billede = itemView.findViewById(R.id.chatImageView);
        }
    }
}
