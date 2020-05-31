package com.example.android.androidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.util.ItemClickListener;
import com.example.android.androidapp.util.ObserverbarListe;
import com.google.android.material.navigation.NavigationView;

public class VaelgChatActivity extends AppCompatActivity implements ItemClickListener {
    DrawerLayout drawerLayout;
    ObserverbarListe<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaelg_chat);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationHjaelper.initialiserMenu(navigationView, drawerLayout);

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Vælg samtale");

        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        BeskedFacade beskedFacade = BeskedFacade.hentInstans();

        chats = (ObserverbarListe<Chat>) beskedFacade.hentNuvaerendeListe();

        VaelgChatAdapter vaelgChatAdapter = new VaelgChatAdapter(brugerFacade.hentAktivBruger().getNavn());
        vaelgChatAdapter.setClickListener(this);
        vaelgChatAdapter.setChats(chats);
        RecyclerView recyclerView = findViewById(R.id.vaelg_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vaelgChatAdapter);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view, int position) {
        final Chat chat = chats.get(position);
        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        String navn = brugerFacade.hentAktivBruger().getNavn();
        String modpart = "";
        if (chat.getDeltagere()[0].equals(navn)) {
            modpart = chat.getDeltagere()[1];
        }
        else {
            modpart = chat.getDeltagere()[0];
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("deltagere", chat.getDeltagere());
        intent.putExtra("emne", chat.getEmne());
        intent.putExtra("modpart", modpart);
        startActivity(intent);
    }

    public void nySamtale(View view) {

    }
}
