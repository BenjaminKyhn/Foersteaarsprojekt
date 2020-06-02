package com.example.android.androidapp.usecases;

public class TraeningsprogramFacade {
    private static TraeningsprogramFacade traeningsprogramFacade;

    private TraeningsprogramFacade() {
        traeningsprogramManager = new TraeningsprogramManager();
    }

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

    public void angivListe(ObserverbarListe<String> liste) {
        traeningsprogramManager.angivListe(liste);
    }

    public ObserverbarListe<String> hentListe() {
        return traeningsprogramManager.hentListe();
    }
}
