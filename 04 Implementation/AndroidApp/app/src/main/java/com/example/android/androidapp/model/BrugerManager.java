package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.persistence.DatabaseManager;

/** @author Tommy **/
class BrugerManager {
    private Bruger aktivBruger;
    private DatabaseManager databaseManager = new DatabaseManager();

    void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        if (aktivBruger != null) {
            throw new BrugerLoggedeIndException();
        }
        String enkrypteretPassword = enkrypterTekst(password);
        Bruger bruger = new Bruger(navn, email, enkrypteretPassword);
        databaseManager.gemBruger(bruger);
        aktivBruger = bruger;
    }

    private String enkrypterTekst(String tekst) {
        return tekst;
    }
}
