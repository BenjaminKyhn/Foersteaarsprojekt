package model;

import entities.Oevelse;

import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramFacade {
    TraeningsprogramManager traeningsprogramManager;

    public TraeningsprogramFacade() {
        traeningsprogramManager = new TraeningsprogramManager();
    }

    public void tilfoejOevelse(String oevelse) {
        traeningsprogramManager.tilfoejOevelse(oevelse);
    }

    public void fjernOevelse(String oevelse) {
        traeningsprogramManager.fjernOevelse(oevelse);
    }

    public void angivListe(ArrayList<String> liste) {
        traeningsprogramManager.angivListe(liste);
    }

    public ArrayList<String> hentListe() {
        return traeningsprogramManager.hentListe();
    }

    public ArrayList<Oevelse> hentOevelser() {
        return traeningsprogramManager.hentOevelser();
    }

    public void setOevelser(ArrayList<Oevelse> oevelser){
        traeningsprogramManager.setOevelser(oevelser);
    }
}
