package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Begivenhed;
import com.example.android.androidapp.entities.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.OverlappendeBegivenhederException;
import com.example.android.androidapp.entities.exceptions.PasswordLaengdeException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmailException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;
import com.example.android.androidapp.entities.exceptions.TomNavnException;
import com.example.android.androidapp.entities.exceptions.TomPasswordException;

import java.util.ArrayList;


/** @author Tommy **/
// Klasse til at håndtere validering af brugerinputs. Brugt både i besked klasser og bruger klasser
class Validering {
    private BrugerManager brugerManager;

    Validering() {
        brugerManager = newBrugerManager();
    }

    Validering(BrugerManager brugerManager) {
        this.brugerManager = brugerManager;
    }

    void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals("")) {
            throw new TomEmailException();
        }
        if (brugerManager.hentBrugerMedEmail(email) != null) {
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

        if (emne.length() > 50) {
            throw new ForMangeTegnException();
        }
    }

    void tjekBesked(String besked) throws ForMangeTegnException, TomBeskedException {
        if (besked.equals("")) {
            throw new TomBeskedException();
        }

        if (besked.length() > 160) {
            throw new ForMangeTegnException();
        }
    }

    void tjekBegivenhed(Begivenhed begivenhed, ArrayList<Begivenhed> gemteBegivenheder) throws OverlappendeBegivenhederException {
        for (Begivenhed gemtBegivenhed : gemteBegivenheder) {
            if (gemtBegivenhed.getStartTidspunkt() <= begivenhed.getStartTidspunkt() && begivenhed.getStartTidspunkt() <= gemtBegivenhed.getStartTidspunkt()) {
                for (String deltager : begivenhed.getDeltagere()) {
                    if (gemtBegivenhed.getDeltagere().contains(deltager)) {
                        throw new OverlappendeBegivenhederException();
                    }
                }
            }
        }
    }

    protected BrugerManager newBrugerManager() {
        return new BrugerManager();
    }

}
