package com.example.android.androidapp.domain;

import com.example.android.androidapp.model.TekstHasher;

/** @author Tommy **/
public class Bruger {
    private String navn;
    private String email;
    private String password;

    public Bruger() {
    }

    public Bruger(String navn, String email, String password) {
        this.navn = navn;
        this.email = email;
        this.password = TekstHasher.hashTekst(password);
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
}
