package com.example.android.androidapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.androidapp.R;
import com.example.android.androidapp.domain.Chat;

import java.util.ArrayList;
import java.util.List;

public class VaelgChatAdapter extends RecyclerView.Adapter<VaelgChatAdapter.VaelgChatHolder> {
    private List<Chat> chats = new ArrayList<>();
    private String aktivBruger;

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

    void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    static class VaelgChatHolder extends RecyclerView.ViewHolder {
        TextView samtalePerson;
        TextView emne;

        VaelgChatHolder(View itemView) {
            super(itemView);
            samtalePerson = itemView.findViewById(R.id.textViewNavn);
            emne = itemView.findViewById(R.id.textViewEmne);
        }
    }
}
