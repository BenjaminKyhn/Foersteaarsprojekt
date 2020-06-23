package com.example.android.androidapp.entities;

import com.example.android.androidapp.model.TekstHasher;

import java.util.ArrayList;

/** @author Tommy **/
// Bruger klassen som har alt information som en bruger behøver i programmet.
// Getters og setters på engelsk da et specifikt navn er nødvendig for at Cloud Firestore kan mappe objektet.
public class Bruger {
    private String navn;
    private String email;
    private String password;
    private String fotoURL;
    private boolean erBehandler;
    private ArrayList<String> behandlere;

    public Bruger() {
    }

    public Bruger(String navn, String email, String password, boolean erBehandler) {
        this.navn = navn;
        this.email = email;
        this.password = TekstHasher.hashTekst(password);
        this.erBehandler = erBehandler;
        behandlere = new ArrayList<>();
    }

    public boolean validerPassword(String password) {
        String hashedePassword = TekstHasher.hashTekst(password);
        return hashedePassword.equals(this.password);
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public boolean isErBehandler() {
        return erBehandler;
    }

    public void setErBehandler(boolean erBehandler) {
        this.erBehandler = erBehandler;
    }

    public ArrayList<String> getBehandlere() {
        return behandlere;
    }

    public void setBehandlere(ArrayList<String> behandlere) {
        this.behandlere = behandlere;
    }
}
