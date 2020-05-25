package com.example.android.androidapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;

public class VaelgChatActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaelg_chat);
        drawerLayout = findViewById(R.id.drawer_layout);

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Vælg samtale");

        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        BeskedFacade beskedFacade = BeskedFacade.hentInstans();

        VaelgChatAdapter vaelgChatAdapter = new VaelgChatAdapter(brugerFacade.hentAktivBruger().getNavn());
        vaelgChatAdapter.setChats(beskedFacade.hentNuvaerendeListe());
        RecyclerView recyclerView = findViewById(R.id.vaelg_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vaelgChatAdapter);
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
