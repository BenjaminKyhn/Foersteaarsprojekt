package com.example.android.androidapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.androidapp.R;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.google.android.material.navigation.NavigationView;

/**@author Kelvin**/
public class OpretBrugerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    BrugerFacade brugerFacade;
    EditText navnInput;
    EditText emailInput;
    EditText passwordInput;
    EditText gentagPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opret_bruger);
        drawerLayout = findViewById(R.id.drawer_layout);
        brugerFacade = BrugerFacade.hentInstans();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationHjaelper.initialiserMenu(navigationView, drawerLayout);

        navnInput = findViewById(R.id.navnFelt);
        emailInput = findViewById(R.id.emailFelt);
        passwordInput = findViewById(R.id.passwordFelt);
        gentagPasswordInput = findViewById(R.id.gentagPasswordFelt);

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Opret bruger");
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void opretBruger(View view) {
        String navn = navnInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String gentagPassword = passwordInput.getText().toString();

        if (!password.equals(gentagPassword)) {
            Toast.makeText(this, "Password'et blev ikke tastede korrekt", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            brugerFacade.opretBruger(navn, email, password);
        } catch (BrugerLoggedeIndException e) {
            Toast.makeText(this, "Bruger må ikke være logged ind.", Toast.LENGTH_SHORT).show();
        }
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
}
