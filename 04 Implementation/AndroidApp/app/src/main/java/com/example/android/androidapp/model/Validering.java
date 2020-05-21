package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.model.exceptions.ForMangeTegnException;
import com.example.android.androidapp.model.exceptions.PasswordLaengdeException;
import com.example.android.androidapp.model.exceptions.TomBeskedException;
import com.example.android.androidapp.model.exceptions.TomEmailException;
import com.example.android.androidapp.model.exceptions.TomEmneException;
import com.example.android.androidapp.model.exceptions.TomNavnException;
import com.example.android.androidapp.model.exceptions.TomPasswordException;
import com.example.android.androidapp.persistence.DatabaseManager;

/** @author Tommy **/
class Validering {
    private DatabaseManager databaseManager = new DatabaseManager();

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

    void tjekPassword(String password) throws TomPasswordException, PasswordLaengdeException {
        if (password.equals("")) {
            throw new TomPasswordException();
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new PasswordLaengdeException();
        }
    }

    void tjekEmne(String emne) throws ForMangeTegnException, TomEmneException {
        if (emne.equals("")) {
            throw new TomEmneException();
        }

        if (emne.length() > 100) {
            throw new ForMangeTegnException();
        }
    }

    void tjekBesked(String besked) throws ForMangeTegnException, TomBeskedException {
        if (besked.equals("")) {
            throw new TomBeskedException();
        }

        if (besked.length() > 1000) {
            throw new ForMangeTegnException();
        }
    }
}
