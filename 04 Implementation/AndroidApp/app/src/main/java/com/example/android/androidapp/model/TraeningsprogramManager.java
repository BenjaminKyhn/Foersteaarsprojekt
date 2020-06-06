package com.example.android.androidapp.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramManager {
    private ArrayList<String> program;
    private PropertyChangeSupport support;

    TraeningsprogramManager() {
        program = new ArrayList<>();
        support = new PropertyChangeSupport(this);
    }

    void tilfoejOevelse(String oevelse) {
        program.add(oevelse);
        support.firePropertyChange("tilfoejOevelse", null, oevelse);
    }

    void fjernOevelse(String oevelse) {
        for (String programoevelse : program) {
            if (programoevelse.equals(oevelse)) {
                program.remove(programoevelse);
                support.firePropertyChange("fjernOevelse", null, programoevelse);
            }
        }
    }

    void angivListe(ArrayList<String> liste) {
        program = liste;
    }

    ArrayList<String> hentListe() {
        return program;
    }

}
