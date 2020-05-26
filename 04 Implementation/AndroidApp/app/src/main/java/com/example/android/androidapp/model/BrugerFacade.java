package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.model.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.model.exceptions.ForkertPasswordException;
import com.example.android.androidapp.model.exceptions.PasswordLaengdeException;
import com.example.android.androidapp.model.exceptions.TomEmailException;
import com.example.android.androidapp.model.exceptions.TomNavnException;
import com.example.android.androidapp.model.exceptions.TomPasswordException;

import java.util.List;

/** @author Tommy **/
public class BrugerFacade {
    private BrugerManager brugerManager;
    private Validering validering;
    private static BrugerFacade brugerFacade;

    private BrugerFacade() {
        validering = new Validering();
        brugerManager = new BrugerManager();
    }

    public static synchronized BrugerFacade hentInstans() {
        if (brugerFacade == null) {
            brugerFacade = new BrugerFacade();
        }
        return brugerFacade;
    }

    public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        validering.tjekEmail(email);
    }

    public void tjekNavn(String navn) throws TomNavnException {
        validering.tjekNavn(navn);
    }

    public void tjekPassword(String password) throws PasswordLaengdeException, TomPasswordException {
        validering.tjekPassword(password);
    }

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        brugerManager.opretBruger(navn, email, password);
    }

    public void sletBruger(Bruger user, String password) throws ForkertPasswordException {
        brugerManager.sletBruger(user, password);
    }

    public boolean logInd(String email, String password) {
        return brugerManager.logInd(email, password);
    }

    public void saetListeAfBrugere(List<Bruger> brugere) {
        validering.setBrugere(brugere);
        brugerManager.setBrugere(brugere);
    }

    public Bruger hentAktivBruger() {
        return brugerManager.getAktivBruger();
    }
}
