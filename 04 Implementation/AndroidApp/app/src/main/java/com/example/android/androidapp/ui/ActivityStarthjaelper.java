package com.example.android.androidapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.model.BrugerFacade;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class ActivityStarthjaelper {

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
            ImageView billede = navigationView.getHeaderView(0).findViewById(R.id.profilBillede);
            String pictureUrl = bruger.getFotoURL();
            Picasso.get().load(pictureUrl).into(billede);
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
                        case R.id.beskeder:
                            context.startActivity(new Intent(context, VaelgChatActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.log_ud:
                            context.startActivity(new Intent(context, MainActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            BrugerFacade.hentInstans().logUd();
                            break;
                        case R.id.ny_besked:
                            DialogFragment nySamtale = new VaelgChatDialog();
                            FragmentActivity activity = (FragmentActivity) navigationView.getContext();
                            nySamtale.show(activity.getSupportFragmentManager(), "nySamtale");
                            break;
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
    static void initialiserActivity(AppCompatActivity activity, DrawerLayout drawerLayout, int include, String headertekst) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        drawerLayout.addView(layoutInflater.inflate(include, drawerLayout, false), 0);

        ImageView menu = drawerLayout.findViewById(R.id.burgerMenu);

        menu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        TextView statusBar = drawerLayout.findViewById(R.id.statusBar);
        statusBar.setText(headertekst);
    }
}
