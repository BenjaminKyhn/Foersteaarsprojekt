package model;

import domain.Bruger;
import model.exceptions.EksisterendeBrugerException;
import model.exceptions.TomEmailException;
import persistence.DatabaseManager;

import java.io.IOException;

/** @author Benjamin */
class Validering {
    private DatabaseManager databaseManager = new DatabaseManager();

    Validering() throws IOException {
    }

    public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals(""))
            throw new TomEmailException();
        Bruger bruger = databaseManager.hentBrugerMedEmail(email);
        if (bruger != null){
            throw new EksisterendeBrugerException();
        }
    }
}
