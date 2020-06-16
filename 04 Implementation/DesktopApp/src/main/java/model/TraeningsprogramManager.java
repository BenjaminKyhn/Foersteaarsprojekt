package model;

import entities.Oevelse;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramManager {
    ArrayList<String> program;
    ArrayList<Oevelse> oevelser;
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

    public void setOevelser(ArrayList<Oevelse> oevelser) {
        this.oevelser = oevelser;
    }
}
