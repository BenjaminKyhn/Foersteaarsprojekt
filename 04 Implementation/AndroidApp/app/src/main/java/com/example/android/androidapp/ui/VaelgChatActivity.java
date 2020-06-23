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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.database.DatabaseManager;
import com.google.android.material.navigation.NavigationView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**@author Tommy**/
public class VaelgChatActivity extends AppCompatActivity implements ItemClickListener, VaelgChatDialog.VaelgChatListener {
    private DrawerLayout drawerLayout;
    private BeskedFacade beskedFacade;
    private BrugerFacade brugerFacade;
    private ArrayList<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_vaelg_chat, "Vælg samtale");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);

        brugerFacade = BrugerFacade.hentInstans();
        beskedFacade = BeskedFacade.hentInstans();

        chats = beskedFacade.hentNuvaerendeListe();

        VaelgChatAdapter vaelgChatAdapter = new VaelgChatAdapter(brugerFacade.hentAktivBruger().getNavn());

        beskedFacade.tilfoejListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("opretChat")) {
                    new DatabaseManager().gemChat((Chat) evt.getNewValue());
                    vaelgChatAdapter.setChats(chats);
                }
            }
        });

        vaelgChatAdapter.setClickListener(this);
        vaelgChatAdapter.setChats(chats);
        RecyclerView recyclerView = findViewById(R.id.vaelg_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vaelgChatAdapter);
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

    @Override
    public void onClick(View view, int position) {
        final Chat chat = chats.get(position);
        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        String navn = brugerFacade.hentAktivBruger().getNavn();
        String modpart = "";
        if (chat.getAfsender().equals(navn)) {
            modpart = chat.getModtager();
        }
        else {
            modpart = chat.getAfsender();
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("afsender", chat.getAfsender());
        intent.putExtra("modtager", chat.getModtager());
        intent.putExtra("emne", chat.getEmne());
        intent.putExtra("modpart", modpart);
        startActivity(intent);
    }

    public void nySamtaleDialog(View view) {
        DialogFragment nySamtale = new VaelgChatDialog();
        nySamtale.show(getSupportFragmentManager(), "nySamtale");
    }

    @Override
    public void nySamtale(String modtager, String emne) {
        String afsender =  brugerFacade.hentAktivBruger().getNavn();
        beskedFacade.setBrugerManager(brugerFacade.hentBrugerManager());
        try {
            beskedFacade.opretChat(afsender, modtager, emne);
        } catch (BrugerFindesIkkeException e) {
            Toast.makeText(this, "Bruger findes ikke", Toast.LENGTH_LONG).show();
        } catch (TomEmneException e) {
            Toast.makeText(this, "Emnet må ikke være tomt", Toast.LENGTH_LONG).show();
        } catch (ForMangeTegnException e) {
            Toast.makeText(this, "Emnet må ikke være mere end 100 tegn", Toast.LENGTH_LONG).show();
        }
    }
}
