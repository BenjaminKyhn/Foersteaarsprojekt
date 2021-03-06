package com.example.android.androidapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.entities.exceptions.BrugerAlleredeLoggedIndException;
import com.example.android.androidapp.database.DatabaseManager;
import com.google.android.material.navigation.NavigationView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**@author Kelvin**/
public class OpretBrugerActivity extends AppCompatActivity {
    /**@author Tommy**/
    private DrawerLayout drawerLayout;
    private BrugerFacade brugerFacade;
    private ProgressDialog progressDialog;
    private EditText navnInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText gentagPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_opret_bruger, "Opret bruger");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);
        brugerFacade = BrugerFacade.hentInstans();

        brugerFacade.tilfoejListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("opretBruger")) {
                    DatabaseManager databaseManager = new DatabaseManager();
                    Bruger bruger = (Bruger) evt.getNewValue();
                    databaseManager.gemBruger(bruger);
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                }
            }
        });



        navnInput = findViewById(R.id.navnFelt);
        emailInput = findViewById(R.id.emailFelt);
        passwordInput = findViewById(R.id.passwordFelt);
        gentagPasswordInput = findViewById(R.id.gentagPasswordFelt);
    }

    public void opretBruger(View view) {
        String navn = navnInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String gentagPassword = gentagPasswordInput.getText().toString();

        if (!password.equals(gentagPassword)) {
            Toast.makeText(this, "Password'et blev ikke tastede korrekt", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = ProgressDialog.show(this, "", "Logger ind...", true);
        try {
            brugerFacade.opretBruger(navn, email, password);
        } catch (BrugerAlleredeLoggedIndException e) {
            progressDialog.dismiss();
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
