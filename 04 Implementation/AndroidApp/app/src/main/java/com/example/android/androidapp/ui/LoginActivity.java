package com.example.android.androidapp.ui;

import androidx.annotation.NonNull;
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
import com.example.android.androidapp.database.DatabaseManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * @author Patrick
 **/
public class LoginActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText editTextEmail;
    EditText editTextPassword;
    BrugerFacade brugerFacade;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        drawerLayout = findViewById(R.id.drawer_layout);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        brugerFacade = BrugerFacade.hentInstans();
        databaseManager = new DatabaseManager();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationHjaelper.initialiserMenu(navigationView, drawerLayout);

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Login");

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
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
        } else {
            super.onBackPressed();
        }
    }

    public void logInd(View view) {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Logger ind...", true);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.hentBrugerMedEmail(email);

        databaseManager.tilfoejListener(evt -> {
            if (evt.getPropertyName().equals("hentBrugerMedEmail")) {
                progressDialog.dismiss();
                Bruger bruger = (Bruger) evt.getNewValue();
                if (bruger == null) {
                    Toast.makeText(getApplicationContext(), "Brugeren findes ikke", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Bruger> brugerList = new ArrayList<>();
                brugerList.add(bruger);
                brugerFacade.saetListeAfBrugere(brugerList);
                boolean loggedeInd = brugerFacade.logInd(email, password);
                if (loggedeInd) {
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Password er ikke korrekt", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}
