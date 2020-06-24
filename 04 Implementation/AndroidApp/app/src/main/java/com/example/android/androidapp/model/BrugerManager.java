package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.exceptions.BrugerAlleredeLoggedIndException;
import com.example.android.androidapp.entities.exceptions.ForkertPasswordException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy **/
// Brugermanageren er den egentlige klasse der er ekspert i at håndtere brugere. Den indeholder en samling af brugere.
class BrugerManager {
    // aktiveBruger er det bruger objekt som er loggede ind.
    private Bruger aktivBruger;
    private ArrayList<Bruger> brugere;
    private PropertyChangeSupport support;

    BrugerManager() {
        support = new PropertyChangeSupport(this);
        brugere = new ArrayList<>();
    }

    void rydObservere() {
        support = new PropertyChangeSupport(this);
    }

    // Usorterede lister itereres igennem for at finde den ønskede bruger.

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

    // På Android app'en må brugeren ikke være loggede ind modsat Desktop app'en hvor det er tilladt.
    void opretBruger(String navn, String email, String password) throws BrugerAlleredeLoggedIndException {
        if (aktivBruger != null) {
            throw new BrugerAlleredeLoggedIndException();
        }
        Bruger bruger = new Bruger(navn, email, password, false);
        brugere.add(bruger);
        aktivBruger = bruger;

        // Observerkald hvor det nye bruger objekt passeres som argument.
        support.firePropertyChange("opretBruger", null, bruger);
    }

    // Brugeren skal angive kontoens password før brugeren slettes.
    void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        if (!bruger.validerPassword(password)) {
            throw new ForkertPasswordException();
        }
        brugere.remove(bruger);
        support.firePropertyChange("sletBruger", null, bruger);
        aktivBruger = null;
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

    Bruger hentAktivBruger() {
        return aktivBruger;
    }

    void tilfoejListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    void fjernListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
