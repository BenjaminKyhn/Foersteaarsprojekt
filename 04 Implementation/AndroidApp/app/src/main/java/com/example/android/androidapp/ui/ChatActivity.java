package com.example.android.androidapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**@author Tommy**/
public class ChatActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextInputEditText beskedFelt;
    private ArrayList<Besked> beskeder;
    private ChatAdapter chatAdapter;
    private ChatPresenter chatPresenter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        Intent intent = getIntent();

        String afsender = intent.getStringExtra("afsender");
        String modtager = intent.getStringExtra("modtager");
        String emne = intent.getStringExtra("emne");
        String modpart = intent.getStringExtra("modpart");

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_chat, modpart);
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);

        beskedFelt = findViewById(R.id.besked_input_edit_text);

        String bruger = BrugerFacade.hentInstans().hentAktivBruger().getNavn();

        chatPresenter = new ChatPresenter(afsender, modtager, emne);
        chatPresenter.setBeskedAfsender(bruger);
        chatPresenter.setBeskedModtager(modpart);
        chatPresenter.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("nyBeskedPresenter")) {
                    chatAdapter.setBeskeder(beskeder);
                    recyclerView.smoothScrollToPosition(beskeder.size() - 1);
                }
            }
        });

        beskeder = chatPresenter.getBeskeder();

        chatAdapter = new ChatAdapter();
        chatAdapter.setBeskeder(beskeder);
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
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
            beskedFelt.setText("");
        } catch (TomBeskedException e) {
            Toast.makeText(this, "Beskeden må ikke være tom", Toast.LENGTH_SHORT).show();
        } catch (ForMangeTegnException e) {
            Toast.makeText(this, "Beskeden må ikke have mere end 1000 tegn", Toast.LENGTH_SHORT).show();
        }
    }
}
