package com.example.android.androidapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.androidapp.R;

/**@author Patrick**/
public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
//        configureLogindButton();
//        configureNyBeskedButton();
//        configureBurgerButton();
//        configureOpretBrugerButton();
    }
//    private void configureLogindButton() {
//        Button changeActivityButton = findViewById(R.id.logind);
//        changeActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            }
//        });
//    }
//    private void configureOpretBrugerButton() {
//        Button changeActivityButton = findViewById(R.id.opretbruger);
//        changeActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, OpretBrugerActivity.class));
//            }
//        });
//    }
//    private void configureNyBeskedButton() {
//        Button changeActivityButton = findViewById(R.id.tempTest);
//        changeActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, NyBeskedActivity.class));
//            }
//        });
//    }
//    private void configureBurgerButton() {
//        ImageView changeActivityButton = findViewById(R.id.burgerMenu);
//        changeActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, NaviView.class));
//            }
//        });
//    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
