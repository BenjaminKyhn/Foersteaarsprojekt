package com.example.android.androidapp.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;

/** @author Tommy **/
// Chat klassen som har alt information som en chat behøver i programmet.
// Getters og setters på engelsk da et specifikt navn er nødvendig for at Cloud Firestore kan mappe objektet.
public class Chat {
    private PropertyChangeSupport support;

    private String afsender;
    private String modtager;
    private String emne;
    private long sidstAktiv;
    private ArrayList<Besked> beskeder = new ArrayList<>();

    public Chat() {
        support = new PropertyChangeSupport(this);
    }

    public Chat(String afsender, String modtager, String emne) {
        this();
        this.afsender = afsender;
        this.modtager = modtager;
        this.emne = emne;
        sidstAktiv = System.currentTimeMillis();
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
            return Long.compare(o2.sidstAktiv, o1.sidstAktiv);
        }
    };

    public String getAfsender() {
        return afsender;
    }

    public void setAfsender(String afsender) {
        this.afsender = afsender;
    }

    public String getModtager() {
        return modtager;
    }

    public void setModtager(String modtager) {
        this.modtager = modtager;
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

    public ArrayList<Besked> hentBeskeder() {
        return beskeder;
    }

    public void setBeskeder(ArrayList<Besked> beskeder) {
        this.beskeder = beskeder;
    }
}
