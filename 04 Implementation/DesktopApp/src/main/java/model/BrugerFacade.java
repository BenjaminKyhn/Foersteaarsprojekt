package model;

import model.exceptions.*;

import java.io.IOException;

/** @author Benjamin */
public class BrugerFacade {
    private Validering validering = new Validering();

    public BrugerFacade() throws IOException {
    }

    public void tjekEmail(String email) throws EksisterendeBrugerException, TomEmailException {
        validering.tjekEmail(email);
    }

    public void tjekNavn(String navn) throws TomNavnException{
        validering.tjekNavn(navn);
    }

    public void tjekPassword(String password) throws PasswordLaengdeException, TomPasswordException {
        validering.tjekPassword(password);
    }
}
