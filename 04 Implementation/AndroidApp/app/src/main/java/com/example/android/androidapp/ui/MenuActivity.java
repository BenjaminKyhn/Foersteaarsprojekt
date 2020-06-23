package com.example.android.androidapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Begivenhed;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.Oevelse;
import com.example.android.androidapp.entities.Traeningsprogram;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BookingFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.database.DatabaseManager;
import com.example.android.androidapp.model.TraeningsprogramFacade;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**@author Tommy**/
public class MenuActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_menu, "Menu");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);

        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        BeskedFacade beskedFacade = BeskedFacade.hentInstans();
        TraeningsprogramFacade traeningsprogramFacade = TraeningsprogramFacade.hentInstans();
        BookingFacade bookingFacade = BookingFacade.hentInstans();

        ArrayList<Chat> chatListe = new ArrayList<>();
        beskedFacade.saetListeAfChats(chatListe);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.hentChatsTilBruger(brugerFacade.hentAktivBruger().getNavn(), chatListe);
        databaseManager.hentBehandlereTilBruger(brugerFacade.hentAktivBruger(), brugerFacade.hentBrugere());

        databaseManager.hentOevelser();
        databaseManager.hentProgramTilBruger(BrugerFacade.hentInstans().hentAktivBruger());
        databaseManager.hentBegivenhederTilBruger(BrugerFacade.hentInstans().hentAktivBruger());

        databaseManager.tilfoejListener(evt -> {
            if (evt.getPropertyName().equals("hentProgramTilBruger")) {
                traeningsprogramFacade.angivProgrammer((ArrayList<Traeningsprogram>) evt.getNewValue());
            }
            if (evt.getPropertyName().equals("hentOevelser")) {
                traeningsprogramFacade.angivOevelser((ArrayList<Oevelse>) evt.getNewValue());
            }
            if (evt.getPropertyName().equals("hentBegivenhederTilBruger")) {
                bookingFacade.angivBegivenheder((ArrayList<Begivenhed>) evt.getNewValue());
            }
        });
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

    public void skiftTilBooking(View view) {
        startActivity(new Intent(this, BookingActivity.class));
    }

    public void skiftTilBeskeder(View view) {
        startActivity(new Intent(this, VaelgChatActivity.class));
    }
    public void skiftTilKalender(View view) {
        startActivity(new Intent(this, KalenderActivity.class));
    }

    public void skiftTilTraening(View view) {
        Intent intent = new Intent(this, TraeningsprogramActivity.class);
        TraeningsprogramFacade traeningsprogramFacade = TraeningsprogramFacade.hentInstans();
        if (traeningsprogramFacade.hentProgrammer() == null || traeningsprogramFacade.hentProgrammer().isEmpty()) {
            intent.putExtra("intetProgram", true);
        }
        else {
            intent.putExtra("intetProgram", false);
        }
        startActivity(intent);
    }
}
