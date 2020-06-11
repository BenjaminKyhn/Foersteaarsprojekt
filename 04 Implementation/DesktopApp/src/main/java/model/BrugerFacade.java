package model;

import entities.Bruger;
import entities.exceptions.*;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author Benjamin
 *  BrugerFacade fungerer som et samlet koblingspunkt for UI-elementer, der vil ændre på brugerelementer. Dens primære
 *  funktion er at sende metodekald videre til enten Validering eller BrugerManager.
 */
public class BrugerFacade {
    private Validering validering;
    private BrugerManager brugerManager;
    private static BrugerFacade brugerFacade;

    /**
     * Denne constructor er private, fordi vi anvender singleton pattern.
     */
    private BrugerFacade() {
        validering = new Validering();
        brugerManager = BrugerManager.getInstance();
    }

    /**
     * Når man skal bruge BrugerFacade kaldes denne metode for at sikre, at der aldrig findes mere end én BrugerFacade.
     * @return  returnerer sit statiske variabel brugerFacade
     */
    public static synchronized BrugerFacade getInstance() {
        if (brugerFacade == null) {
            brugerFacade = new BrugerFacade();
        }
        return brugerFacade;
    }

    /**
     *  @author Kelvin
     *  Denne metode kaldes, når en brugers erBehandler-felt skal ændres.
     * @param patient brugeren, som skal have tilknyttet en behandler
     * @param behandler brugeren, som skal tilknyttes den anden bruger
     */
    public void tilknytBehandler(Bruger patient, Bruger behandler) throws ForkertRolleException, BehandlerFindesAlleredeException {
        brugerManager.tilknytBehandler(patient, behandler);
    }

    /**
     * @author Benjamin
     * Metoden kaldes, når gyldigheden af en email skal tjekkes.
     * @param email den email, der skal tjekkes
     * @throws EksisterendeBrugerException når der allerede findes en bruger med den email
     * @throws TomEmailException når email String er tom
     */
    public void tjekEmail(String email) throws EksisterendeBrugerException, TomEmailException {
        validering.tjekEmail(email);
    }

    /**
     * Metoden kaldes, når gyldigheden af et navn skal tjekkes.
     * @param navn det navn, der skal tjekkes
     * @throws TomNavnException når navn String er tom
     */
    public void tjekNavn(String navn) throws TomNavnException {
        validering.tjekNavn(navn);
    }

    /**
     * Metoden kaldes, når gyldigheden af et password skal tjekkes.
     * @param password det password, der skal tjekkes
     * @throws TomNavnException når password String er tom
     */
    public void tjekPassword(String password) throws PasswordLaengdeException, TomPasswordException {
        validering.tjekPassword(password);
    }

    /**
     * Metoden kaldes, når der skal oprettes en ny Bruger
     * @param navn navnet på brugeren
     * @param email brugeren emails
     * @param password brugerens password
         * @param erBehandler true, hvis brueren er behandler og false, hvis brugeren er patient
     * @throws BrugerErIkkeBehandlerException når en ikke-behandler prøver at kalde opretBruger
     * @throws TomPasswordException når passwordet er tomt
     * @throws PasswordLaengdeException når passwordets længde er ugyldigt
     * @throws TomNavnException når navnet er tomt
     * @throws EksisterendeBrugerException når der allerede eksisterer en bruger med samme email
     * @throws TomEmailException når email er tomt
     */
    public void opretBruger(String navn, String email, String password, boolean erBehandler) throws BrugerErIkkeBehandlerException, TomPasswordException, PasswordLaengdeException, TomNavnException, EksisterendeBrugerException, TomEmailException {
        brugerManager.opretBruger(navn, email, password, erBehandler);
    }

    /**
     * Metoden kaldes, når en Bruger slettes fra systemet.
     * @param bruger den bruger, der skal slettes
     * @param password det indtastede password
     * @throws ForkertPasswordException når det indtastede password ikke matcher brugerens password
     */
    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        brugerManager.sletBruger(bruger, password);
    }

    /**
     * Metoden kaldes, når en patients Bruger skal slettes fra systemet. Denne metode er ret ens med sletBruger, men
     * eksisterer, fordi behandlere og systemadministratoren skal kunne slette patienter fra systemet uden at kende
     * deres password.
     * @param bruger den bruger, der skal slettes
     * @param email den indtastede email
     * @throws ForkertEmailException når den indtastede email ikke matcher brugerens email
     * @see public void sletBruger(Bruger bruger, String password)
     */
    public void sletPatient(Bruger bruger, String email) throws ForkertEmailException {
        brugerManager.sletPatient(bruger, email);
    }

    /**
     * Metoden kaldes, når en bruger indtaster sine brugeroplysninger for at logge ind i systemet.
     * @param email den indtastede email
     * @param password det indtastede password
     * @return returnerer true, hvis email og password matcher. Vi returnerer en boolean, så vi kan lave tjek på email
     * og password.
     * @throws ForkertPasswordException når email og password ikke matcher
     */
    public boolean logInd(String email, String password) throws ForkertPasswordException {
        return brugerManager.logInd(email, password);
    }

    /**
     * Metoden kaldes, når brugeren logger ud af systemet
     */
    public void logUd() {
        brugerManager.logUd();
    }

    public Bruger getAktivBruger() {
        return brugerManager.getAktivBruger();
    }

    public Bruger hentBrugerMedNavn(String navn){
        return brugerManager.hentBrugerMedNavn(navn);
    }

    public void setBrugere(ArrayList<Bruger> brugere){
        brugerManager.setBrugere(brugere);
    }

    public ArrayList<Bruger> hentBrugere(){
        return brugerManager.hentBrugere();
    }

    /** @author Kelvin */
    public ArrayList<Bruger> hentPatienter() {
        return brugerManager.hentPatienter();
    }

    /** @author Benjamin */
    public ArrayList<Bruger> hentBehandlere() {
        return brugerManager.hentBehandlere();
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        brugerManager.tilfoejObserver(listener);
    }
}