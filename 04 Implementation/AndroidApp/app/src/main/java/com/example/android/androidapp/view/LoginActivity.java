package com.example.android.androidapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.androidapp.R;
import com.example.android.androidapp.model.BrugerFacade;

/**@author Patrick**/
public class LoginActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText editTextEmail;
    EditText editTextPassword;
    BrugerFacade brugerFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        drawerLayout = findViewById(R.id.drawer_layout);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        brugerFacade = BrugerFacade.hentInstans();

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
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        boolean loggedeInd = brugerFacade.logInd(email, password);
        if (loggedeInd) {
            startActivity(new Intent(this, MenuActivity.class));
        }
        else {
            Toast.makeText(this, "Login fejlede", Toast.LENGTH_SHORT).show();
        }
    }
}
