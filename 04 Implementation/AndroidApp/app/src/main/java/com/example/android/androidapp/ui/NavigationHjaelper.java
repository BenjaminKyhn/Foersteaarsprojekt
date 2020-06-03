package com.example.android.androidapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.usecases.BrugerFacade;
import com.google.android.material.navigation.NavigationView;

class NavigationHjaelper {

    static void initialiserMenu(final NavigationView navigationView, final DrawerLayout drawerLayout) {
        Bruger bruger = BrugerFacade.hentInstans().hentAktivBruger();
        if (bruger == null) {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            navigationView.inflateHeaderView(R.layout.navigation_header_gaest);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu_gaest);
            return;
        }
        else {
            TextView navn = navigationView.getHeaderView(0).findViewById(R.id.personNavn);
            navn.setText(bruger.getNavn());
            TextView mail = navigationView.getHeaderView(0).findViewById(R.id.personMail);
            mail.setText(bruger.getEmail());
        }
        
        navigationView.removeHeaderView(navigationView.findViewById(R.id.header));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (BrugerFacade.hentInstans().hentAktivBruger() != null) {
                    Context context = navigationView.getContext();
                    switch (item.getItemId()) {
                        case R.id.start_menu:
                            context.startActivity(new Intent(context, MenuActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.indbakke:
                            context.startActivity(new Intent(context, VaelgChatActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.log_ud:
                            context.startActivity(new Intent(context, MainActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            BrugerFacade.hentInstans().logUd();
                            break;
                        /**case R.id.ny_besked:
                            context.startActivity(new Intent(context, VaelgChatActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;*/
                        case R.id.kalender:
                            context.startActivity(new Intent(context, KalenderActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
