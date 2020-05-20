package com.example.android.androidapp.model;

import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.model.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.model.exceptions.TomEmailException;
import com.example.android.androidapp.model.exceptions.TomNavnException;

/** @author Tommy **/
public class BrugerFacade {
    private BrugerManager brugerManager = new BrugerManager();
    private Validering validering = new Validering();

    public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        validering.tjekEmail(email);
    }

    public void tjekNavn(String navn) throws TomNavnException {
        validering.tjekNavn(navn);
    }

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        brugerManager.opretBruger(navn, email, password);
    }
}
