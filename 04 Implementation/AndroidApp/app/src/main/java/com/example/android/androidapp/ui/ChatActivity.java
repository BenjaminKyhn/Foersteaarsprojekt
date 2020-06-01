package com.example.android.androidapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.usecases.BrugerFacade;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.database.DatabaseManager;
import com.google.android.material.navigation.NavigationView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    DatabaseManager databaseManager;
    EditText beskedFelt;
    ArrayList<Besked> beskeder;
    ChatAdapter chatAdapter;
    ChatPresenter chatPresenter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationHjaelper.initialiserMenu(navigationView, drawerLayout);

        beskedFelt = findViewById(R.id.editTextBesked);

        String bruger = BrugerFacade.hentInstans().hentAktivBruger().getNavn();

        Intent intent = getIntent();


        String afsender = intent.getStringExtra("afsender");
        String modtager = intent.getStringExtra("modtager");
        String emne = intent.getStringExtra("emne");
        String modpart = intent.getStringExtra("modpart");

        databaseManager = new DatabaseManager();

        chatPresenter = new ChatPresenter(afsender, modtager, emne);
        chatPresenter.setBeskedAfsender(bruger);
        chatPresenter.setBeskedModtager(modpart);
        chatPresenter.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("nyBesked")) {
                    chatAdapter.setBeskeder(beskeder);
                    recyclerView.smoothScrollToPosition(beskeder.size() - 1);
                }
            }
        });

        beskeder = chatPresenter.getBeskeder();

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.observerBeskederFraFirestore(chatPresenter.getChat());

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText(modpart);

        chatAdapter = new ChatAdapter();
        chatAdapter.setBeskeder(beskeder);
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
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

    public void sendBesked(View view) {
        String besked = beskedFelt.getText().toString();
        try {
            chatPresenter.sendBesked(besked);
        } catch (TomBeskedException e) {
            Toast.makeText(this, "Beskeden må ikke være tom", Toast.LENGTH_SHORT).show();
        } catch (ForMangeTegnException e) {
            Toast.makeText(this, "Beskeden må ikke have mere end 1000 tegn", Toast.LENGTH_SHORT).show();
        }
    }
}
