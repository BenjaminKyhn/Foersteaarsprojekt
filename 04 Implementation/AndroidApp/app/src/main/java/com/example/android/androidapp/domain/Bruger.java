package com.example.android.androidapp.domain;

/** @author Tommy **/
public class Bruger {
    String navn;
    String mail;
    String password;

    public Bruger(String navn, String mail, String password) {
        this.navn = navn;
        this.mail = mail;
        this.password = password;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
