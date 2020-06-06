package com.example.android.androidapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Chat;

import java.util.ArrayList;

public class VaelgChatAdapter extends RecyclerView.Adapter<VaelgChatAdapter.VaelgChatHolder> {
    private ArrayList<Chat> chats = new ArrayList<>();
    private String aktivBruger;
    private ItemClickListener itemClickListener;

    public VaelgChatAdapter(String aktivBruger) {
        this.aktivBruger = aktivBruger;
    }

    @NonNull
    @Override
    public VaelgChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaelg_chat_adapter_item, parent, false);
        return new VaelgChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaelgChatHolder holder, int position) {
        if (chats.get(position).getAfsender().equals(aktivBruger)) {
            holder.samtalePerson.setText(chats.get(position).getModtager());
        }
        else {
            holder.samtalePerson.setText(chats.get(position).getAfsender());
        }
        holder.emne.setText(chats.get(position).getEmne());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    void setClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public class VaelgChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView samtalePerson;
        TextView emne;

        VaelgChatHolder(View itemView) {
            super(itemView);
            samtalePerson = itemView.findViewById(R.id.textViewNavn);
            emne = itemView.findViewById(R.id.textViewEmne);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
