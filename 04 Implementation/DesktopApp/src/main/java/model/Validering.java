package model;

import entities.Bruger;
import entities.exceptions.*;

/**
 * @author Benjamin
 * Klassen eksisterer for at lave tjeks på diverse informationer om Brugere, Chats og Beskeder
 */
class Validering {
    BrugerManager brugerManager;

    /**
     * Denne constructor kaldes, når en ny instans af Validering instantieres og instantierer samtidig brugerManager.
     */
    Validering() {
        brugerManager = newBrugerManager();
    }

    /**
     * Denne construtor bruges i unittest
     * @param brugerManager Validering skal bruge en BrugerManager.
     */
    Validering(BrugerManager brugerManager) {
        this.brugerManager = brugerManager;
    }

    /**
     * Metoden tjekker gyldigheden af en email ved at kalde hentBrugerMedEmail på brugerManager.
     * @param email den indtastede email
     * @throws TomEmailException hvis email er tom
     * @throws EksisterendeBrugerException hvis en Bruger med den indtastede email allerede eksisterer i brugerManagers
     * liste af brugere.
     */
    public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        if (email.equals(""))
            throw new TomEmailException();
        Bruger bruger = brugerManager.hentBrugerMedEmail(email);
        if (bruger != null){
            throw new EksisterendeBrugerException();
        }
    }

    /**
     * Metoden tjekker gyldigheden af et navn.
     * @param navn det indtastede navn
     * @throws TomNavnException hvis navn er tomt
     */
    public void tjekNavn(String navn) throws TomNavnException {
        if (navn.equals(""))
            throw new TomNavnException();
    }

    /**
     * Metoden tjekker gyldigheden af et password
     * @param password det indtastede password
     * @throws TomPasswordException hvis passwordet er tomt
     * @throws PasswordLaengdeException hvis passwordets længde ikke er 6-20 tegn
     */
    public void tjekPassword(String password) throws TomPasswordException, PasswordLaengdeException{
        if (password.equals(""))
            throw new TomPasswordException();
        if (password.length() < 6 || password.length() > 20)
            throw new PasswordLaengdeException();
    }

    /**
     * Metoden tjekker gyldigheden af et emne
     * @param emne det indtastede emne
     * @throws TomEmneException hvis emnet er tomt
     * @throws ForMangeTegnException hvis emnets længde er over 50
     */
    public void tjekEmne(String emne) throws TomEmneException, ForMangeTegnException {
        if (emne.equals(""))
            throw new TomEmneException();
        if (emne.length() > 50)
            throw new ForMangeTegnException();
    }

    /**
     * Metoden tjekker gyldigheden af en besked
     * @param besked den indtastede besked
     * @throws TomBeskedException hvis beskeden er tom
     * @throws ForMangeTegnException hvis beskeden længde er over 160
     */
    public void tjekBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        if (besked.equals(""))
            throw new TomBeskedException();
        if (besked.length() > 160)
            throw new ForMangeTegnException();
    }

    /**
     * Metoden bruges i unittest
     * @return returnerer singleton pattern af BrugerManager
     */
    protected BrugerManager newBrugerManager() {
        return BrugerManager.getInstance();
    }
}