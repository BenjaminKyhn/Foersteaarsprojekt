package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.exceptions.BrugerAlleredeLoggedIndException;
import com.example.android.androidapp.entities.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.entities.exceptions.ForkertPasswordException;
import com.example.android.androidapp.entities.exceptions.PasswordLaengdeException;
import com.example.android.androidapp.entities.exceptions.TomEmailException;
import com.example.android.androidapp.entities.exceptions.TomNavnException;
import com.example.android.androidapp.entities.exceptions.TomPasswordException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy **/
// Denne klasse fungere som udvendig access point til alt der har med brugere at gøre.
public class BrugerFacade {
    private BrugerManager brugerManager;
    private Validering validering;
    private static BrugerFacade brugerFacade;

    private BrugerFacade() {
        brugerManager = new BrugerManager();
        validering = new Validering(brugerManager);
    }

    public void rydObservere() {
        brugerManager.rydObservere();
    }

    // Beskedfacaden bruger singleton design pattern for nemt at bevare brugere mellem activities.
    public static synchronized BrugerFacade hentInstans() {
        if (brugerFacade == null) {
            brugerFacade = new BrugerFacade();
        }
        return brugerFacade;
    }

    public static BrugerFacade getInstance() {
        return hentInstans();
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

    public void opretBruger(String navn, String email, String password) throws BrugerAlleredeLoggedIndException {
        brugerManager.opretBruger(navn, email, password);
    }

    public void sletBruger(Bruger user, String password) throws ForkertPasswordException {
        brugerManager.sletBruger(user, password);
    }

    public boolean logInd(String email, String password) {
        return brugerManager.logInd(email, password);
    }

    public void logUd() {
        brugerManager.logUd();
    }

    public void saetListeAfBrugere(ArrayList<Bruger> brugere) {
        brugerManager.setBrugere(brugere);
    }

    public Bruger hentAktivBruger() {
        return brugerManager.hentAktivBruger();
    }

    public ArrayList<Bruger> hentBrugere() {
        return brugerManager.hentBrugere();
    }

    public Bruger hentBrugerMedNavn(String navn){
        return brugerManager.hentBrugerMedNavn(navn);
    }

    public ArrayList<String> hentBehandlereNavne() {
        return brugerManager.hentBehandlereNavne();
    }

    public BrugerManager hentBrugerManager() {
        return brugerManager;
    }

    public void tilfoejListener(PropertyChangeListener listener) {
        brugerManager.tilfoejListener(listener);
    }

    public void fjernListener(PropertyChangeListener listener) {
        brugerManager.tilfoejListener(listener);
    }

    public void setBrugere(ArrayList<Bruger> hentBrugere) {

    }
}
