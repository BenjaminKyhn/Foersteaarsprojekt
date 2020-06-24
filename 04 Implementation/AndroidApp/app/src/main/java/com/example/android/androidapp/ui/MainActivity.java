package com.example.android.androidapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.androidapp.R;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BookingFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.model.TraeningsprogramFacade;
import com.google.android.material.navigation.NavigationView;

/**@author Patrick**/
public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_main, "Velkommen");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);


        // Nullify alle lister og listeners så programmet er som det startede. Nødvendig pga. singleton pattern.
        BrugerFacade.hentInstans().saetListeAfBrugere(null);
        BrugerFacade.hentInstans().rydObservere();
        BookingFacade.hentInstans().angivBegivenheder(null);
        BookingFacade.hentInstans().rydObservere();
        BeskedFacade.hentInstans().saetListeAfChats(null);
        BeskedFacade.hentInstans().rydObservere();
        TraeningsprogramFacade.hentInstans().angivOevelser(null);
        TraeningsprogramFacade.hentInstans().angivProgrammer(null);
        TraeningsprogramFacade.hentInstans().rydObservere();
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

    public void skiftTilLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void skiftTilOpretBruger(View view) {
        startActivity(new Intent(this, OpretBrugerActivity.class));
    }
}
