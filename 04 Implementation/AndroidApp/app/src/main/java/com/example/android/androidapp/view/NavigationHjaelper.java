package com.example.android.androidapp.view;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.androidapp.R;
import com.example.android.androidapp.model.BrugerFacade;
import com.google.android.material.navigation.NavigationView;

class NavigationHjaelper {

    static void initialiserMenu(final NavigationView navigationView, final DrawerLayout drawerLayout) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (BrugerFacade.hentInstans().hentAktivBruger() != null) {
                    Context context = navigationView.getContext();
                    switch (item.getItemId()) {
                        case R.id.indbakke:
                            context.startActivity(new Intent(context, VaelgChatActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.log_ud:
                            context.startActivity(new Intent(context, MainActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            BrugerFacade.hentInstans().logUd();
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
