package entities;

import unittests.usecases.ObserverbarListe;

import java.util.ArrayList;

/** @author Benjamin */
public class Bruger {
    private String navn;
    private String email;
    private String password;
    private String fotoURL;
    private boolean erBehandler;
    private ArrayList<String> behandlere;

    public Bruger(){
    }

    public Bruger(String navn, String email, String password, boolean erBehandler){
        this.navn = navn;
        this.email = email;
        this.password = password;
        this.erBehandler = erBehandler;
    }

    public ArrayList<String> getBehandlere() {return behandlere;}

    public void setBehandlere(ArrayList<String> behandlere) {this.behandlere = behandlere;}

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
}