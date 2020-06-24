package com.example.android.androidapp.model;


import com.example.android.androidapp.entities.Begivenhed;
import com.example.android.androidapp.entities.exceptions.OverlappendeBegivenhederException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Benjamin
 */
public class BookingManager {
    private PropertyChangeSupport support;
    private ArrayList<Begivenhed> begivenheder;

    BookingManager() {
        support = new PropertyChangeSupport(this);
    }

    void rydObservere() {
        support = new PropertyChangeSupport(this);
    }

    public ArrayList<Begivenhed> hentBegivenheder() {
        return begivenheder;
    }

    public ArrayList<Begivenhed> hentBegivenhederFraBehandlerMedDato(String behandler, long dato) {
        ArrayList<Begivenhed> filtreredeListe = new ArrayList<>();
        for (Begivenhed begivenhed : begivenheder) {
            if (begivenhed.getDeltagere().contains(behandler)) {
                if (begivenhed.getStartTidspunkt() >= dato && begivenhed.getSlutTidspunkt() <= (dato + 86400000)) {
                    filtreredeListe.add(begivenhed);
                }
            }
        }
        return filtreredeListe;
    }

    public void angivBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
    }

    /**
     * Denne metode bruges, når der slettes en entry fra kalenderUI'et
     * @param id id fra den entry, som blev slettet
     */
    public void sletBegivenhed(String id) {
        for (int i = 0; i < begivenheder.size(); i++) {
            if (begivenheder.get(i).getId().equals(id)){
                Begivenhed begivenhed = begivenheder.get(i);
                begivenheder.remove(begivenhed);
                support.firePropertyChange("sletBegivenhed", null, begivenhed);
            }
        }
    }

    public void tjekBegivenhed(Begivenhed begivenhed) throws OverlappendeBegivenhederException {
        Validering validering = new Validering();
        validering.tjekBegivenhed(begivenhed, begivenheder);
    }

    /**
     * Denne metode bruges, når der oprettes en ny entry i kalenderUI'et
     * @param begivenhed entry laves om til et Begivenhedobjekt, før det gemmes i listen og databasen
     */
    public void gemBegivenhed(Begivenhed begivenhed) throws OverlappendeBegivenhederException {
        Validering validering = new Validering();
        validering.tjekBegivenhed(begivenhed, begivenheder);
        begivenheder.add(begivenhed);
        support.firePropertyChange("gemBegivenhed", null, begivenhed);
    }

    /**
     * Denne metode bruges, når der foretages ændringer på en eller flere entries i calendar-UI'et.
     * Entries i UI'et gemmes allerede i databasen, når de oprettes eller slettes. Men da Entry-klassen ikke tillader at
     * observere på ændringer, så må vi oprette en ny liste med alle Begivenheder og derefter overskrive hver enkelt
     * Begivenhed i databasen.
     * @param begivenheder listen med alle begivenheder, som eksisterer som entries i UI'et på det tidspunkt, hvor
     *                     metoden bliver kaldt
     */
    public void gemBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
        for (int i = 0; i < begivenheder.size(); i++) {
            Begivenhed begivenhed = begivenheder.get(i);
            support.firePropertyChange("gemBegivenhed", null, begivenhed);
        }
    }

    public void tilfoejObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
