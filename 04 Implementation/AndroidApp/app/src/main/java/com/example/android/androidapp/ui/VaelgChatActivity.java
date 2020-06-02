package com.example.android.androidapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.usecases.BeskedFacade;
import com.example.android.androidapp.usecases.BrugerFacade;
import com.example.android.androidapp.database.DatabaseManager;
import com.example.android.androidapp.usecases.ObserverbarListe;
import com.google.android.material.navigation.NavigationView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class VaelgChatActivity extends AppCompatActivity implements ItemClickListener, VaelgChatDialog.VaelgChatListener {
    DrawerLayout drawerLayout;
    BeskedFacade beskedFacade;
    BrugerFacade brugerFacade;
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

        brugerFacade = BrugerFacade.hentInstans();
        beskedFacade = BeskedFacade.hentInstans();

        chats = (ObserverbarListe<Chat>) beskedFacade.hentNuvaerendeListe();

        final VaelgChatAdapter vaelgChatAdapter = new VaelgChatAdapter(brugerFacade.hentAktivBruger().getNavn());

        chats.tilfoejListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("nyAddition")) {
                    new DatabaseManager().gemChat((Chat) evt.getNewValue());
                    vaelgChatAdapter.setChats(chats);
                }
            }
        });

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
        if (chat.getAfsender().equals(navn)) {
            modpart = chat.getModtager();
        }
        else {
            modpart = chat.getAfsender();
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("afsender", chat.getAfsender());
        intent.putExtra("modtager", chat.getModtager());
        intent.putExtra("emne", chat.getEmne());
        intent.putExtra("modpart", modpart);
        startActivity(intent);
    }

    public void nySamtaleDialog(View view) {
        DialogFragment nySamtale = new VaelgChatDialog();
        nySamtale.show(getSupportFragmentManager(), "nySamtale");
    }

    @Override
    public void nySamtale(String modtager, String emne) {
        String afsender =  brugerFacade.hentAktivBruger().getNavn();
        beskedFacade.opretChat(afsender, modtager, emne);
    }
}