package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.model.exceptions.ForkertPasswordException;
import java.util.List;

/** @author Tommy **/
class BrugerManager {
    private Bruger aktivBruger;
    private List<Bruger> brugere;

    void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        if (aktivBruger != null) {
            throw new BrugerLoggedeIndException();
        }
        Bruger bruger = new Bruger(navn, email, password, false);
        brugere.add(bruger);
        aktivBruger = bruger;
    }

    void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        if (!bruger.validerPassword(password)) {
            throw new ForkertPasswordException();
        }
        brugere.remove(bruger);
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

    void setBrugere(List<Bruger> brugere) {
        this.brugere = brugere;
    }

    Bruger getAktivBruger() {
        return aktivBruger;
    }
}
