package com.example.android.androidapp.model;

import java.util.ArrayList;

/** @author Tommy */
// Facade til træningsprogram funktioner. Ikke videre udviklet på nuværende tidspunkt 11/06/20
public class TraeningsprogramFacade {
    private static TraeningsprogramFacade traeningsprogramFacade;

    private TraeningsprogramFacade() {
        traeningsprogramManager = new TraeningsprogramManager();
    }

    // Singleton design pattern
    public static synchronized TraeningsprogramFacade hentInstans() {
        if (traeningsprogramFacade == null) {
            traeningsprogramFacade = new TraeningsprogramFacade();
        }
        return traeningsprogramFacade;
    }
    TraeningsprogramManager traeningsprogramManager;

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
}
