package com.example.android.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.domain.ObserverbarListe;
import com.example.android.androidapp.persistence.DatabaseManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class MainActivity extends AppCompatActivity {
    ObserverbarListe liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.hentBrugereReference().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                liste = new ObserverbarListe(queryDocumentSnapshots.toObjects(Bruger.class));
                liste.tilfoejListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("nyAddition")) {
                            String string = evt.getNewValue().toString();
                            Log.d("test", string);
                        }
                    }
                });
                String string = liste.getClass().toString();
                Log.d("test", string);
            }
        });
    }

    public void testAdd(View view) {
        Bruger bruger = new Bruger("test", "test", "test");
        liste.add(bruger);
        Log.d("test", liste.size() + "");
        Log.d("test", liste.get(liste.size() - 1).toString());
    }
}
