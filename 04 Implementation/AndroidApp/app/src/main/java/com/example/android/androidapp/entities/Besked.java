package com.example.android.androidapp.entities;

/** @author Tommy **/
// Besked klassen som har alt information som en besked behøver i programmet.
// Getters og setters på engelsk da et specifikt navn er nødvendig for at Cloud Firestore kan mappe objektet.
public class Besked {
    private String besked;
    private long tidspunkt;
    private String afsender;
    private String modtager;

    public Besked() {
    }

    public Besked(String besked, long tidspunkt, String afsender, String modtager) {
        this.besked = besked;
        this.tidspunkt = tidspunkt;
        this.afsender = afsender;
        this.modtager = modtager;
    }

    public String getBesked() {
        return besked;
    }

    public void setBesked(String besked) {
        this.besked = besked;
    }

    public long getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(long tidspunkt) {
        this.tidspunkt = tidspunkt;
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
}
