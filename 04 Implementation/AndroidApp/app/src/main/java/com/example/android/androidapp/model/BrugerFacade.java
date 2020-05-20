package com.example.android.androidapp.model;

import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;

/** @author Tommy **/
public class BrugerFacade {
    private BrugerManager brugerManager = new BrugerManager();

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        brugerManager.opretBruger(navn, email, password);
    }
}
