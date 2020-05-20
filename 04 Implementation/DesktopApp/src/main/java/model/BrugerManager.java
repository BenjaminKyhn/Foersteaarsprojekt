package model;

import domain.Bruger;
import model.exceptions.BrugerLoggedIndException;
import model.exceptions.ForkertPasswordException;
import persistence.DatabaseManager;

import java.io.IOException;

/** @author Benjamin */
public class BrugerManager {
    private Bruger aktivBruger;
    private DatabaseManager databaseManager = new DatabaseManager();

    public BrugerManager() throws IOException {
    }

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedIndException {
        if (aktivBruger != null){
            throw new BrugerLoggedIndException();
        }

        String enkrypteretPassword = enkrypterTekst(password);
        Bruger bruger = new Bruger(navn, email, enkrypteretPassword);
        databaseManager.gemBruger(bruger);
        aktivBruger = bruger;
    }

    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        String enkrypteretPassword = enkrypterTekst(password);
        String enkrypteretBrugerPassword = bruger.getPassword();

        if (!enkrypteretPassword.equals(enkrypteretBrugerPassword))
            throw new ForkertPasswordException();

        databaseManager.sletBruger(bruger);

        aktivBruger = null;
    }

    public String enkrypterTekst(String password){
        return password;
    }
}
