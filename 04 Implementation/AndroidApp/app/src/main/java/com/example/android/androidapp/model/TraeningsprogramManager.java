package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Oevelse;
import com.example.android.androidapp.entities.Traeningsprogram;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
// træningsprogramManager klasse instantieret i traeningsprogramFacade
public class TraeningsprogramManager {
    private ArrayList<String> program;
    ArrayList<Oevelse> oevelser;
    ArrayList<Traeningsprogram> programmer;
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

    void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    ArrayList<Oevelse> hentOevelser() {
        return oevelser;
    }

    void angivOevelser(ArrayList<Oevelse> oevelser) {
        this.oevelser = oevelser;
    }

    ArrayList<Traeningsprogram> hentProgrammer() {
        return programmer;
    }

    void angivProgrammer(ArrayList<Traeningsprogram> programmer) {
        this.programmer = programmer;
    }

}
