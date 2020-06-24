package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Oevelse;
import com.example.android.androidapp.entities.Traeningsprogram;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
// træningsprogramManager klasse instantieret i traeningsprogramFacade
public class TraeningsprogramManager {
    private ArrayList<Oevelse> oevelser;
    private ArrayList<Traeningsprogram> programmer;
    private PropertyChangeSupport support;

    TraeningsprogramManager() {
        support = new PropertyChangeSupport(this);
        programmer = new ArrayList<>();
        oevelser = new ArrayList<>();
    }

    void rydObservere() {
        support = new PropertyChangeSupport(this);
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
