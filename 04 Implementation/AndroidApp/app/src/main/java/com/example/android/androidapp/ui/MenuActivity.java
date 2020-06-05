package com.example.android.androidapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.database.DatabaseManager;
import com.example.android.androidapp.model.ObserverbarListe;
import com.example.android.androidapp.model.TraeningsprogramFacade;
import com.google.android.material.navigation.NavigationView;

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
        statusBar.setText("Menu");
        brugerFacade = BrugerFacade.hentInstans();
        beskedFacade = BeskedFacade.hentInstans();

        chatListe = new ObserverbarListe<>();
        beskedFacade.saetListeAfChats(chatListe);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.hentChatsTilBruger(brugerFacade.hentAktivBruger().getNavn(), chatListe);

        ObserverbarListe<String> program = new ObserverbarListe<>();
        TraeningsprogramFacade traeningsprogramFacade = TraeningsprogramFacade.hentInstans();
        databaseManager.hentProgram(BrugerFacade.hentInstans().hentAktivBruger(), program);
        traeningsprogramFacade.angivListe(program);
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

    public void skiftTilIndbakke(View view) {
        startActivity(new Intent(this, VaelgChatActivity.class));
    }
    public void skiftTilKalender(View view) {
        startActivity(new Intent(this, KalenderActivity.class));
    }

    public void skiftTilTraening(View view) {
        Intent intent = new Intent(this, TraeningsprogramActivity.class);
        intent.putExtra("intetProgram", false);
        startActivity(intent);
    }
}
