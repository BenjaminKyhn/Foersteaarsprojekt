package model;

import domain.Bruger;
import model.exceptions.*;
import persistence.DatabaseManager;

/** @author Benjamin */
class Validering {
    /** Der kan ikke være 2 instanser af DatabaseManager, så derfor bruger vi getInstance() */
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    Validering() {
    }

    public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals(""))
            throw new TomEmailException();
        Bruger bruger = databaseManager.hentBrugerMedEmail(email);
        if (bruger != null){
            throw new EksisterendeBrugerException();
        }
    }

    public void tjekNavn(String navn) throws TomNavnException {
        if (navn.equals(""))
            throw new TomNavnException();
    }

    public void tjekPassword(String password) throws TomPasswordException, PasswordLaengdeException{
        if (password.equals(""))
            throw new TomPasswordException();
        if (password.length() < 6 || password.length() > 20)
            throw new PasswordLaengdeException();
    }

    public void tjekEmne(String emne) throws TomEmneException, ForMangeTegnException {
        if (emne.equals(""))
            throw new TomEmneException();
        if (emne.length() > 100)
            throw new ForMangeTegnException();
    }

    public void tjekBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        if (besked.equals(""))
            throw new TomBeskedException();
        if (besked.length() > 1000)
            throw new ForMangeTegnException();
    }
}