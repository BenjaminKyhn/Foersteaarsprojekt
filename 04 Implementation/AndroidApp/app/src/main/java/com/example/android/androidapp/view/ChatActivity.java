package com.example.android.androidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidapp.R;
import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.model.exceptions.ForMangeTegnException;
import com.example.android.androidapp.model.exceptions.TomBeskedException;
import com.example.android.androidapp.persistence.DatabaseManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    BeskedFacade beskedFacade;
    DatabaseManager databaseManager;
    EditText beskedFelt;
    String bruger;
    String modpart;
    ArrayList<Besked> beskeder;
    Chat chat;
    ChatAdapter chatAdapter;
    RecyclerView recyclerView;
    boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        drawerLayout = findViewById(R.id.drawer_layout);

        beskedFelt = findViewById(R.id.editTextBesked);

        bruger = BrugerFacade.hentInstans().hentAktivBruger().getNavn();

        Intent intent = getIntent();
        String modtager = intent.getStringExtra("modtager");
        String afsender = intent.getStringExtra("afsender");
        String emne = intent.getStringExtra("emne");
        modpart = intent.getStringExtra("modpart");

        beskedFacade = BeskedFacade.hentInstans();
        databaseManager = new DatabaseManager();

        chat = beskedFacade.hentChat(afsender, modtager, emne);
        beskeder = new ArrayList<>();
        beskeder = chat.getBeskeder();

        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        firstTime = true;

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText(modpart);

        chatAdapter = new ChatAdapter();
        chatAdapter.setBeskeder(beskeder);
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        Query query = databaseManager.hentChatsReference().whereEqualTo("modtager", modpart).whereEqualTo("afsender", afsender).whereEqualTo("emne", emne);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentReference documentReference = queryDocumentSnapshots.getDocuments().get(0).getReference();
                documentReference.collection("beskeder").orderBy("tidspunkt").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (firstTime) {
                            firstTime = false;
                            return;
                        }
                        List<DocumentChange> documentSnapshots = queryDocumentSnapshots.getDocumentChanges();
                        Besked besked = documentSnapshots.get(documentSnapshots.size() - 1).getDocument().toObject(Besked.class);
                        beskeder.add(besked);
                    }
                });

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

    public void sendBesked(View view) {
        String besked = beskedFelt.getText().toString();
        try {
            beskedFacade.tjekBesked(besked);
        } catch (TomBeskedException e) {
            Toast.makeText(this, "Beskeden må ikke være tom", Toast.LENGTH_SHORT).show();
        } catch (ForMangeTegnException e) {
            Toast.makeText(this, "Beskeden må ikke have mere end 1000 tegn", Toast.LENGTH_SHORT).show();
        }
        beskedFacade.sendBesked(besked, chat, bruger, modpart);
        beskeder = chat.getBeskeder();
        chatAdapter.setBeskeder(beskeder);
        recyclerView.scrollToPosition(beskeder.size() - 1);
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.opdaterChat(chat);
    }
}
