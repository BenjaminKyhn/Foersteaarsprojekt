package com.example.android.androidapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.example.android.androidapp.R;
import com.example.android.androidapp.database.DatabaseManager;
import com.example.android.androidapp.entities.Begivenhed;
import com.example.android.androidapp.entities.exceptions.OverlappendeBegivenhederException;
import com.example.android.androidapp.model.BookingFacade;
import com.example.android.androidapp.model.BrugerFacade;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TidsrumDialog.TidsrumDialogListener {
    private DrawerLayout drawerLayout;
    private EditText editTextDato, editTextTid;
    private Spinner spinner;
    private DatePickerDialog datePickerDialog;
    private FrameLayout frameLayout;
    private long dato, start, slut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActivityStarthjaelper.initialiserActivity(this, drawerLayout, R.layout.include_booking, "Booking");
        ActivityStarthjaelper.initialiserMenu(navigationView, drawerLayout);

        spinner = findViewById(R.id.spinnerBehandler);

        ArrayList<String> behandlere = BrugerFacade.hentInstans().hentBehandlereNavne();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, behandlere);

        spinner.setAdapter(adapter);

        editTextDato = findViewById(R.id.editTextDato);
        editTextDato.setKeyListener(null);

        editTextTid = findViewById(R.id.editTextTidsrum);
        editTextTid.setKeyListener(null);

        initialiserDatePicker();

        editTextDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        editTextTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TidsrumDialog();
                dialogFragment.show(getSupportFragmentManager(), "vaelgTidsrum");
            }
        });

        frameLayout = findViewById(R.id.bookingFragmentFrameLayout);


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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String stringDato = dayOfMonth + "/" + (month + 1) + "/" + year;

        editTextDato.setText(stringDato);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(stringDato);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = date.getTime();
        dato = milliseconds;
        ArrayList<Begivenhed> begivenheder = BookingFacade.hentInstans().hentBegivenhederFraBehandlerMedDato(spinner.getSelectedItem().toString(), milliseconds);
        if (begivenheder.size() <= 0) {
            getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new IngenBegivenhederFragment()).commit();
        }
        else {
            BegivenhederFragment begivenhederFragment = new BegivenhederFragment();
            getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), begivenhederFragment).commit();
            begivenhederFragment.angivBegivenheder(begivenheder);
        }

    }

    private void initialiserDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int cYear, cMonth, cDayOfMonth;
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, this, cYear, cMonth, cDayOfMonth);
    }

    public void bekraeft(View view) {
        if (editTextTid.getText().toString().isEmpty() || editTextDato.getText().toString().isEmpty()) {
            return;
        }
        String patient = BrugerFacade.hentInstans().hentAktivBruger().getNavn();
        String behandler = spinner.getSelectedItem().toString();

        String id = UUID.randomUUID().toString();
        String titel = "Patientbooking";
        long startTid = dato + start;
        long slutTid = dato + slut;
        ArrayList<String> deltagere = new ArrayList<>();
        deltagere.add(patient);
        deltagere.add(behandler);

        Begivenhed begivenhed = new Begivenhed(titel, patient, startTid, slutTid, id, deltagere);

        try {
            BookingFacade.hentInstans().tjekBegivenhed(begivenhed);
        } catch (OverlappendeBegivenhederException e) {
            Toast.makeText(this, "Det valgte tidsrum overlapper med eksisterende tid\nVælg et andet tidspunkt", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Bekræft bookning?")
                .setMessage("Er du sikker?")
                .setPositiveButton("Ja", (dialog1, which) -> {
                    bekraeftede(begivenhed);
                }).setNegativeButton("Nej", (dialog2, which) -> {
                    dialog2.dismiss();
                }).create();
        dialog.show();
    }

    public void bekraeftede(Begivenhed begivenhed) {
        BookingFacade bookingFacade = BookingFacade.hentInstans();

        DatabaseManager databaseManager = new DatabaseManager();
        bookingFacade.tilfoejObserver(evt -> {
            if (evt.getPropertyName().equals("gemBegivenhed")) {
                databaseManager.gemBegivenhed((Begivenhed) evt.getNewValue());
                Toast.makeText(this, "Booking bekræftede", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        try {
            bookingFacade.gemBegivenhed(begivenhed);
        } catch (OverlappendeBegivenhederException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void angivTidsrum(String start, String slut) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date date1 = simpleDateFormat.parse(start);
            Date date2 = simpleDateFormat.parse(slut);
            this.start = date1.getTime() + 3600000;
            this.slut = date2.getTime() + 3600000;

            String tekst = start + " - " + slut;
            editTextTid.setText(tekst);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
