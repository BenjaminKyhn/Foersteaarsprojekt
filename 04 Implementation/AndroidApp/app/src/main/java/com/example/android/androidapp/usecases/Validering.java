package com.example.android.androidapp.usecases;

import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.PasswordLaengdeException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmailException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;
import com.example.android.androidapp.entities.exceptions.TomNavnException;
import com.example.android.androidapp.entities.exceptions.TomPasswordException;

import java.util.List;

/** @author Tommy **/
class Validering {
    private List<Bruger> brugere;

    void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals("")) {
            throw new TomEmailException();
        }
        for (Bruger bruger : brugere) {
            if (bruger.getEmail().equals(email)) {
                throw new EksisterendeBrugerException();
            }
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

    void setBrugere(List<Bruger> brugere) {
        this.brugere = brugere;
    }

}
