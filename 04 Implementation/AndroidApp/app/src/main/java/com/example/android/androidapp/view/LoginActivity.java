package com.example.android.androidapp.view;

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
import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.persistence.DatabaseManager;
import com.example.android.androidapp.util.ObserverbarListe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**@author Patrick**/
public class LoginActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText editTextEmail;
    EditText editTextPassword;
    BrugerFacade brugerFacade;
    ObserverbarListe<Bruger> brugere;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        drawerLayout = findViewById(R.id.drawer_layout);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        brugerFacade = BrugerFacade.hentInstans();
        brugere = new ObserverbarListe<>();
        brugerFacade.saetListeAfBrugere(brugere);
        databaseManager = new DatabaseManager();

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
        }
        else {
            super.onBackPressed();
        }
    }

    public void logInd(View view) {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Logger ind...", true);
        progressDialog.show();

        databaseManager.hentBrugereReference().document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Bruger bruger = documentSnapshot.toObject(Bruger.class);
                        List<Bruger> list = new ArrayList<>();
                        list.add(bruger);
                        brugerFacade.saetListeAfBrugere(list);
                        boolean loggedeInd = brugerFacade.logInd(email, password);
                        if (loggedeInd) {
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Password er ikke korrekt", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Brugeren findes ikke", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}
