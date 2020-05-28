package com.example.android.androidapp.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;

/** @author Tommy **/
public class Chat {
    private PropertyChangeSupport support;

    private String afsender;
    private String modtager;
    private String emne;
    private String sidstAktiv;
    private ArrayList<Besked> beskeder = new ArrayList<>();

    private Chat() {
        support = new PropertyChangeSupport(this);
    }

    public Chat(String afsender, String modtager, String emne) {
        this();
        this.afsender = afsender;
        this.modtager = modtager;
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
            return o2.sidstAktiv.compareTo(o1.sidstAktiv);
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

    public String getSidstAktiv() {
        return sidstAktiv;
    }

    public void setSidstAktiv(String sidstAktiv) {
        this.sidstAktiv = sidstAktiv;
    }

    public ArrayList<Besked> getBeskeder() {
        return beskeder;
    }

    public void setBeskeder(ArrayList<Besked> beskeder) {
        this.beskeder = beskeder;
    }
}
