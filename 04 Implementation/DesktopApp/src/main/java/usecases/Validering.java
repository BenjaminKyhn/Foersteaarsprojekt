package usecases;

import entities.Bruger;
import entities.exceptions.*;

/** @author Benjamin */
class Validering {
    BrugerManager brugerManager;
    Validering() {
        brugerManager = newBrugerManager();
    }

    public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals(""))
            throw new TomEmailException();
        Bruger bruger = brugerManager.hentBrugerMedEmail(email);
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
    protected BrugerManager newBrugerManager() {
        return BrugerManager.getInstance();
    }
}