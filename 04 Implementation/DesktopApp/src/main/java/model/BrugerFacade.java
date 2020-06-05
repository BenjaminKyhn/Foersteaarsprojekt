package model;

import entities.Bruger;
import entities.exceptions.*;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * @author Benjamin
 */
public class BrugerFacade {
    private Validering validering;
    private BrugerManager brugerManager;
    private static BrugerFacade brugerFacade;

    public BrugerFacade() {
        validering = new Validering();
        brugerManager = BrugerManager.getInstance();
    }

    public static synchronized BrugerFacade getInstance() {
        if (brugerFacade == null) {
            brugerFacade = new BrugerFacade();
        }
        return brugerFacade;
    }

    /** @author Kelvin */
    public void tilknytBehandler(Bruger patient, Bruger behandler) throws ForkertRolleException, BehandlerFindesAlleredeException {
        brugerManager.tilknytBehandler(patient, behandler);
    }

    /** @author Benjamin */
    public void tjekEmail(String email) throws EksisterendeBrugerException, TomEmailException {
        validering.tjekEmail(email);
    }

    public void tjekNavn(String navn) throws TomNavnException {
        validering.tjekNavn(navn);
    }

    public void tjekPassword(String password) throws PasswordLaengdeException, TomPasswordException {
        validering.tjekPassword(password);
    }

    public void opretBruger(String navn, String email, String password, boolean erBehandler) throws BrugerErIkkeBehandlerException, TomPasswordException, PasswordLaengdeException, TomNavnException, EksisterendeBrugerException, TomEmailException {
        brugerManager.opretBruger(navn, email, password, erBehandler);
    }

    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        brugerManager.sletBruger(bruger, password);
    }

    public boolean logInd(String email, String password) throws ForkertPasswordException {
        return brugerManager.logInd(email, password);
    }

    public void logUd() {
        brugerManager.logUd();
    }

    public Bruger getAktivBruger() {
        return brugerManager.getAktivBruger();
    }

    public Bruger hentBrugerMedNavn(String navn){
        return brugerManager.hentBrugerMedNavn(navn);
    }

    public void setBrugere(List<Bruger> brugere){
        brugerManager.setBrugere(brugere);
    }

    public List<Bruger> hentBrugere(){
        return brugerManager.hentBrugere();
    }

    /** @author Kelvin */
    public List<Bruger> hentPatienter() {
        return brugerManager.hentPatienter();
    }

    /** @author Benjamin */
    public List<Bruger> hentBehandlere() {
        return brugerManager.hentBehandlere();
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        brugerManager.tilfoejObserver(listener);
    }
}