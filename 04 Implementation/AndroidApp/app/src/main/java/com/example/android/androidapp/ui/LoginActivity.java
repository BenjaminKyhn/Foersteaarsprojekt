package com.example.android.androidapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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
    private DrawerLayout drawerLayout;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Switch huskLogin;
    private BrugerFacade brugerFacade;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_login, "Login");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        huskLogin = findViewById(R.id.switchLogin);

        SharedPreferences gemtLogin = getSharedPreferences("login", MODE_PRIVATE);
        String email = gemtLogin.getString("brugerEmail", null);
        String password = gemtLogin.getString("brugerPassword", null);

        if (email != null && password != null) {
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            huskLogin.setChecked(true);
        }

        brugerFacade = BrugerFacade.hentInstans();
        databaseManager = new DatabaseManager();
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
                    SharedPreferences saveLogin = getSharedPreferences("login", MODE_PRIVATE);
                    if (huskLogin.isChecked()) {
                        saveLogin.edit()
                                .putString("brugerEmail", email)
                                .putString("brugerPassword", password)
                                .apply();
                    }
                    else {
                        saveLogin.edit().clear().apply();
                    }
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Password er ikke korrekt", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}
