package com.example.android.androidapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.androidapp.R;
/**@author Kelvin**/
public class IndbakkeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indbakke);
//        configureLaesBeskedButton();
        drawerLayout = findViewById(R.id.drawer_layout);

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Indbakke");
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

    /**@author Patrick**/
    private void configureLaesBeskedButton() {
        TextView changeActivityButton1 = findViewById(R.id.beskedEmne1);
        changeActivityButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
        TextView changeActivityButton2 = findViewById(R.id.besked1);
        changeActivityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
        TextView changeActivityButton3 = findViewById(R.id.beskedEmne2);
        changeActivityButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
        TextView changeActivityButton4 = findViewById(R.id.besked2);
        changeActivityButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
    }
}
