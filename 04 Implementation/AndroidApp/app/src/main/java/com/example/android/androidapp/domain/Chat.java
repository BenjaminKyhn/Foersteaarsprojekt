package com.example.android.androidapp.domain;

import com.example.android.androidapp.util.ObserverbarListe;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;

/** @author Tommy **/
public class Chat {
    private PropertyChangeSupport support;

    private String[] deltagere;
    private String emne;
    private long sidstAktiv;
    private ObserverbarListe<Besked> beskeder = new ObserverbarListe<>();

    private Chat() {
        support = new PropertyChangeSupport(this);
    }

    public Chat(String[] deltagere, String emne) {
        this();
        this.deltagere = deltagere;
        this.emne = emne;
    }

    public void tilfoejObserver(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    public void tilfoejBesked(Besked besked) {
        beskeder.add(besked);
        support.firePropertyChange("nyBesked", null, this);
    }

    public static Comparator<Chat> sorterVedSidstAktiv = new Comparator<Chat>() {
        @Override
        public int compare(Chat o1, Chat o2) {
            return Long.compare(o1.sidstAktiv, o2.sidstAktiv);
        }
    };

    public String[] getDeltagere() {
        return deltagere;
    }

    public void setDeltagere(String[] deltagere) {
        this.deltagere = deltagere;
    }

    public String getEmne() {
        return emne;
    }

    public void setEmne(String emne) {
        this.emne = emne;
    }

    public long getSidstAktiv() {
        return sidstAktiv;
    }

    public void setSidstAktiv(long sidstAktiv) {
        this.sidstAktiv = sidstAktiv;
    }

    public ObserverbarListe<Besked> getBeskeder() {
        return beskeder;
    }

    public void setBeskeder(ObserverbarListe<Besked> beskeder) {
        this.beskeder = beskeder;
    }
}
