package model;

import entities.Oevelse;
import entities.Traeningsprogram;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramFacade {
    TraeningsprogramManager traeningsprogramManager;
    private static TraeningsprogramFacade traeningsprogramFacade;

    public TraeningsprogramFacade() {
        traeningsprogramManager = new TraeningsprogramManager();
    }

    public static synchronized TraeningsprogramFacade getInstance() {
        if (traeningsprogramFacade == null) {
            traeningsprogramFacade = new TraeningsprogramFacade();
        }
        return traeningsprogramFacade;
    }

    public void tilfoejOevelse(String oevelse) {
        traeningsprogramManager.tilfoejOevelse(oevelse);
    }

    public void fjernOevelse(String oevelse) {
        traeningsprogramManager.fjernOevelse(oevelse);
    }

    public void gemProgram(Traeningsprogram program){
        traeningsprogramManager.gemProgram(program);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        traeningsprogramManager.tilfoejObserver(listener);
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

    public void angivOevelser(ArrayList<Oevelse> oevelser){
        traeningsprogramManager.angivOevelser(oevelser);
    }

    public ArrayList<Traeningsprogram> hentProgrammer() {
        return traeningsprogramManager.hentProgrammer();
    }

    public void angivProgrammer(ArrayList<Traeningsprogram> programmer){
        traeningsprogramManager.angivProgrammer(programmer);
    }
}
