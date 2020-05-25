package com.example.android.androidapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.persistence.DatabaseManager;
import com.example.android.androidapp.util.ObserverbarListe;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;

/**@author Patrick**/
public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    BrugerFacade brugerFacade;
    BeskedFacade beskedFacade;
    ObserverbarListe<Chat> chatListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        drawerLayout = findViewById(R.id.drawer_layout);

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Menu");
        brugerFacade = BrugerFacade.hentInstans();
        beskedFacade = BeskedFacade.hentInstans();

        chatListe = new ObserverbarListe<>();
        beskedFacade.saetListeAfChats(chatListe);

        DatabaseManager databaseManager = new DatabaseManager();
        Query query1 = databaseManager.hentChatsReference().whereEqualTo("afsender", brugerFacade.hentAktivBruger().getNavn());
        Query query2 = databaseManager.hentChatsReference().whereEqualTo("modtager", brugerFacade.hentAktivBruger().getNavn());

        query1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Chat> temp = queryDocumentSnapshots.toObjects(Chat.class);
                chatListe.addAll(temp);
                Collections.sort(chatListe, Chat.sorterVedSidstAktiv);
            }
        });

        query2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Chat> temp = queryDocumentSnapshots.toObjects(Chat.class);
                chatListe.addAll(temp);
                Collections.sort(chatListe, Chat.sorterVedSidstAktiv);
            }
        });
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

//    public void skiftTilIndbakke(View view) {
//        startActivity(new Intent(this, IndbakkeActivity.class));
//    }

    public void skiftTilIndbakke(View view) {
        startActivity(new Intent(this, VaelgChatActivity.class));
    }
}
