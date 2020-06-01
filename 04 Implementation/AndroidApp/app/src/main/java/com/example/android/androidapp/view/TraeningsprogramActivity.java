package com.example.android.androidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.androidapp.R;
import com.google.android.material.navigation.NavigationView;

public class TraeningsprogramActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean intetProgram = intent.getBooleanExtra("intetProgram", true);
        if (intetProgram) {
            setContentView(R.layout.activity_intet_traening);
        }
        else {
            setContentView(R.layout.activity_traening);
            initialiserTraeningslayout();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationHjaelper.initialiserMenu(navigationView, drawerLayout);
        ImageView menu = findViewById(R.id.burgerMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        TextView statusBar = findViewById(R.id.statusBar);
        statusBar.setText("Træningsprogram");

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

    private void initialiserTraeningslayout() {
        ListView listView = findViewById(R.id.listViewTraening);
        String[] programmer = {"Hoftebøjer", "Nakke", "Siddende glidende knæ- og ankelbevægelse"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, programmer);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                String videoPath;
                switch (position) {
                    case 0:
                        videoPath = "android.resource://" + getPackageName() + "/" + R.raw.hofteboejer;
                        break;
                    case 1:
                        videoPath = "android.resource://" + getPackageName() + "/" + R.raw.nakke;
                        break;
                    case 2:
                        videoPath = "android.resource://" + getPackageName() + "/" + R.raw.siddende_glidende_knae_og_ankelbevaegelse;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }
                intent.putExtra("videoPath", videoPath);
                startActivity(intent);
            }
        });
    }
}