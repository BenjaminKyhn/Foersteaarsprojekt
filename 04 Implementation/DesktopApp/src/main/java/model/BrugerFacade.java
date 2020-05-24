package model;

import domain.Bruger;
import model.exceptions.*;

import java.io.IOException;

/**
 * @author Benjamin
 */
public class BrugerFacade {
    private Validering validering;
    private BrugerManager brugerManager;
    private static BrugerFacade brugerFacade;

    public BrugerFacade() throws IOException {
        validering = new Validering();
        brugerManager = BrugerManager.getInstance();
    }

    public static synchronized BrugerFacade getInstance() throws IOException {
        if (brugerFacade == null) {
            brugerFacade = new BrugerFacade();
        }
        return brugerFacade;
    }

    public void tjekEmail(String email) throws EksisterendeBrugerException, TomEmailException {
        validering.tjekEmail(email);
    }

    public void tjekNavn(String navn) throws TomNavnException {
        validering.tjekNavn(navn);
    }

    public void tjekPassword(String password) throws PasswordLaengdeException, TomPasswordException {
        validering.tjekPassword(password);
    }

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedIndException {
        brugerManager.opretBruger(navn, email, password);
    }

    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        brugerManager.sletBruger(bruger, password);
    }

    public void logInd(String email, String password) {
        brugerManager.logInd(email, password);
    }

    public void logUd() {
        brugerManager.logUd();
    }

    public Bruger getAktivBruger() {
        return brugerManager.getAktivBruger();
    }
}
