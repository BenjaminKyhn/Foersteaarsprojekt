package com.example.android.androidapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.androidapp.R;
import com.google.android.material.navigation.NavigationView;
/**@author PATRICK**/
public class KalenderActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView kalenderBehandlinger;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_kalender, "Din kalender");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        kalenderBehandlinger = (TextView) findViewById(R.id.kalenderBehandlinger);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String dato = dayOfMonth + "/" + (month + 1) + "/" + year;
                kalenderBehandlinger.setText(dato);
            }
        });
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
