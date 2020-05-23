package com.example.android.androidapp.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy **/
public class Chat {
    private PropertyChangeSupport support;

    private String afsender;
    private String modtager;
    private String emne;
    private ArrayList<Besked> beskeder = new ArrayList<>();

    public Chat() {
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

    public ArrayList<Besked> getBeskeder() {
        return beskeder;
    }

    public void setBeskeder(ArrayList<Besked> beskeder) {
        this.beskeder = beskeder;
    }
}
