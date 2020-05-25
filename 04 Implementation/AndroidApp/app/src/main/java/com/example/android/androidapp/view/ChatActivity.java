package com.example.android.androidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.androidapp.R;
import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.BeskedFacade;

public class ChatActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    BeskedFacade beskedFacade;
    Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaelg_chat);
        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        String modtager = intent.getStringExtra("modtager");
        String afsender = intent.getStringExtra("afsender");
        String emne = intent.getStringExtra("emne");
        String modpart = intent.getStringExtra("modpart");

        beskedFacade = BeskedFacade.hentInstans();
        chat = beskedFacade.hentChat(afsender, modtager, emne);

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText(modpart);
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
}
