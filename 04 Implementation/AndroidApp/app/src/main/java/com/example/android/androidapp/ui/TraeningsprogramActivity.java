package com.example.android.androidapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.Oevelse;
import com.example.android.androidapp.entities.Traeningsprogram;
import com.example.android.androidapp.model.BrugerFacade;
import com.example.android.androidapp.model.TraeningsprogramFacade;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**@author Tommy**/
public class TraeningsprogramActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ArrayList<Oevelse> oevelser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        boolean intetProgram = intent.getBooleanExtra("intetProgram", true);
        if (intetProgram) {
            ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_intet_traening, "Træningsprogram");
        }
        else {
            ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_traening, "Træningsprogram");
            initialiserTraeningslayout();
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);
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

    private void initialiserTraeningslayout() {
        Bruger aktiv = BrugerFacade.hentInstans().hentAktivBruger();
        final ListView listView = findViewById(R.id.listViewTraening);
        ArrayList<Traeningsprogram> programmer = TraeningsprogramFacade.hentInstans().hentProgrammer();
        oevelser = TraeningsprogramFacade.hentInstans().hentOevelser();
        ArrayList<String> oevelseNavne = new ArrayList<>();
        for (Traeningsprogram program : programmer) {
            if (program.getPatientEmail().equals(aktiv.getEmail())) {
                oevelseNavne.addAll(program.getOevelser());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, oevelseNavne);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                String valgtFraListen = listView.getItemAtPosition(position).toString();
                Oevelse valgteOevelse = null;
                for (Oevelse oevelse : oevelser) {
                    if (oevelse.getNavn().equals(valgtFraListen)) {
                        valgteOevelse = oevelse;
                        break;
                    }
                }
                intent.putExtra("videoNavn", valgtFraListen);
                assert valgteOevelse != null;
                intent.putExtra("videoURL", valgteOevelse.getVideoURL() + "&hd=1");
                startActivity(intent);
            }
        });
    }
}
