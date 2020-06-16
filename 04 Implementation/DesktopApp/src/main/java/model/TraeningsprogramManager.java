package model;

import entities.Oevelse;
import entities.Traeningsprogram;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramManager {
    ArrayList<String> program;
    ArrayList<Oevelse> oevelser;
    ArrayList<Traeningsprogram> programmer;
    PropertyChangeSupport support;

    public TraeningsprogramManager() {
        program = new ArrayList<>();
        support = new PropertyChangeSupport(this);
    }

    public void tilfoejOevelse(String oevelse) {
        program.add(oevelse);
        support.firePropertyChange("tilfoejOevelse", null, oevelse);
    }

    public void fjernOevelse(String oevelse) {
        if (program.removeIf(programoevelse -> programoevelse.equals(oevelse))) {
            support.firePropertyChange("fjernOevelse", null, oevelse);
        }
    }

    public void angivListe(ArrayList<String> liste) {
        program = liste;
    }

    public ArrayList<String> hentListe() {
        return program;
    }

    public ArrayList<Oevelse> hentOevelser() {
        return oevelser;
    }

    public void angivOevelser(ArrayList<Oevelse> oevelser) {
        this.oevelser = oevelser;
    }

    public ArrayList<Traeningsprogram> hentProgrammer() {
        return programmer;
    }

    public void angivProgrammer(ArrayList<Traeningsprogram> programmer) {
        this.programmer = programmer;
    }
}
