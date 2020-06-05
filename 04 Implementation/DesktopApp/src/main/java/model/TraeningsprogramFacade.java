package model;

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

    public void angivListe(ObserverbarListe<String> liste) {
        traeningsprogramManager.angivListe(liste);
    }

    public ObserverbarListe<String> hentListe() {
        return traeningsprogramManager.hentListe();
    }
}
