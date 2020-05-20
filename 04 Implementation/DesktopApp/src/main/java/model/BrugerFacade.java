package model;

import model.exceptions.EksisterendeBrugerException;
import model.exceptions.TomEmailException;

import java.io.IOException;

/** @author Benjamin */
public class BrugerFacade {
    private Validering validering = new Validering();

    public BrugerFacade() throws IOException {
    }

    public void tjekEmail(String email) throws EksisterendeBrugerException, TomEmailException {
        validering.tjekEmail(email);
    }
}
