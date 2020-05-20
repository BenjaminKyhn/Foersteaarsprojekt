package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.model.exceptions.TomEmailException;
import com.example.android.androidapp.model.exceptions.TomNavnException;
import com.example.android.androidapp.persistence.DatabaseManager;

/** @author Tommy **/
class Validering {
    DatabaseManager databaseManager = new DatabaseManager();

    void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals("")) {
            throw new TomEmailException();
        }
        Bruger bruger = databaseManager.hentBrugerMedEmail(email);
        if (bruger != null) {
            throw new EksisterendeBrugerException();
        }
    }

    void tjekNavn(String navn) throws TomNavnException {
        if (navn.equals("")) {
            throw new TomNavnException();
        }

    }
}
