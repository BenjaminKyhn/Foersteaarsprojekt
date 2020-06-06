package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.entities.exceptions.ForkertPasswordException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/** @author Tommy **/
class BrugerManager {
    private Bruger aktivBruger;
    private ArrayList<Bruger> brugere;
    private PropertyChangeSupport support;

    BrugerManager() {
        support = new PropertyChangeSupport(this);
        brugere = new ArrayList<>();
    }

    Bruger hentBrugerMedNavn(String navn) {
        for (Bruger bruger : brugere) {
            if (bruger.getNavn().equals(navn)) {
                return bruger;
            }
        }
        return null;
    }

    Bruger hentBrugerMedEmail(String email) {
        for (Bruger bruger : brugere) {
            if (bruger.getEmail().equals(email)) {
                return bruger;
            }
        }
        return null;
    }

    void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        if (aktivBruger != null) {
            throw new BrugerLoggedeIndException();
        }
        Bruger bruger = new Bruger(navn, email, password, false);
        brugere.add(bruger);
        aktivBruger = bruger;
        support.firePropertyChange("opretBruger", null, bruger);
    }

    void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        if (!bruger.validerPassword(password)) {
            throw new ForkertPasswordException();
        }
        brugere.remove(bruger);
        aktivBruger = null;
        support.firePropertyChange("sletBruger", null, bruger);
    }

    boolean logInd(String email, String password) {
        boolean loggedeInd = false;
        for (Bruger bruger : brugere) {
            if (bruger.getEmail().equals(email)) {
                if (bruger.validerPassword(password)) {
                    aktivBruger = bruger;
                    loggedeInd = true;
                }
            }
        }
        return loggedeInd;
    }

    void logUd() {
        if (aktivBruger != null) {
            aktivBruger = null;
        }
    }

    void setBrugere(ArrayList<Bruger> brugere) {
        this.brugere = brugere;
    }

    public ArrayList<Bruger> hentBrugere() {
        return brugere;
    }

    public ArrayList<String> hentBehandlereNavne() {
        ArrayList<String> behandlere = new ArrayList<>();
        for (Bruger bruger : brugere) {
            if (bruger.isErBehandler()) {
                behandlere.add(bruger.getNavn());
            }
        }
        return behandlere;
    }

    Bruger getAktivBruger() {
        return aktivBruger;
    }

    void tilfoejListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    void fjernListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
