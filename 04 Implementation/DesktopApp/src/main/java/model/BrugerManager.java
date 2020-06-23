package model;

import entities.Bruger;
import entities.exceptions.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * @author Benjamin
 * BrugerManager håndterer skabelse og slettelse af brugerobjekter
 */
public class BrugerManager {
    private Bruger aktivBruger;
    private static BrugerManager brugerManager;
    private ArrayList<Bruger> brugere;
    private TekstHasher tekstHasher;
    private Validering validering;
    private PropertyChangeSupport support;

    /**
     * Denne constructor er private, fordi vi anvender singleton pattern.
     */
    BrugerManager() {
        tekstHasher = new TekstHasher();
        validering = newValidering();
        support = new PropertyChangeSupport(this);
    }

    /**
     * Når man skal bruge BrugerManager kaldes denne metode for at sikre, at der aldrig findes mere end én BrugerManager.
     * @return  returnerer sit statiske variabel brugerManager
     */
    public static synchronized BrugerManager getInstance() {
        if (brugerManager == null) {
            brugerManager = new BrugerManager();
        }
        return brugerManager;
    }

    public void rydObservere() {
        support = new PropertyChangeSupport(this);
    }

    /** @author Kelvin */
    public void tilknytBehandler(Bruger patient, Bruger behandler) throws ForkertRolleException, BehandlerFindesAlleredeException {
        if (patient.isErBehandler())
            throw new ForkertRolleException();
        if (!behandler.isErBehandler()) {
            throw new ForkertRolleException();
        }
        if (patient.getBehandlere().contains(behandler.getNavn()))
            throw new BehandlerFindesAlleredeException();
        String navn = behandler.getNavn();
        patient.getBehandlere().add(navn);

        support.firePropertyChange("Ny Behandler", null, patient);
    }

    /**
     * @author Benjamin
     * Denne metode kaldes, når en Bruger skal oprettes. Hvis brugeren allerede er logged ind, laves et tjek, om
     * brugeren er behandler, da patienter ikke kan oprette brugere, når de er logged ind.
     * De indtastede oplysninger tjekkes først for gyldighed og derefter kaldes opretBrugerService-metoden
     * @param navn navnet på brugeren
     * @param email brugerens email
     * @param password brugerens password
     * @param erBehandler true, hvis brueren er behandler og false, hvis brugeren er patient
     * @throws BrugerErIkkeBehandlerException når en ikke-behandler prøver at kalde opretBruger
     * @throws TomPasswordException når passwordet er tomt
     * @throws PasswordLaengdeException når passwordets længde er ugyldigt
     * @throws TomNavnException når navnet er tomt
     * @throws EksisterendeBrugerException når der allerede eksisterer en bruger med samme email
     * @throws TomEmailException når email er tomt
     * @see private void opretBrugerService(String navn, String email, String password, boolean erBehandler)
     */
    public void opretBruger(String navn, String email, String password, boolean erBehandler) throws BrugerErIkkeBehandlerException, TomNavnException, EksisterendeBrugerException, TomEmailException, PasswordLaengdeException, TomPasswordException {
        if (aktivBruger != null){
            if (!aktivBruger.isErBehandler()) {
                throw new BrugerErIkkeBehandlerException();
            }
            validering.tjekNavn(navn);
            validering.tjekEmail(email);
            validering.tjekPassword(password);
            opretBrugerService(navn, email, password, erBehandler);
        }
        else {
            opretBrugerService(navn, email, password, erBehandler);
        }
    }

    /**
     * Metoden kaldes, når kontrollen er nået forbi opretBruger-metoden.
     * @param navn navnet på brugeren
     * @param email brugerens email
     * @param password brugerens indtastede password
     * @param erBehandler true, hvis brueren er behandler og false, hvis brugeren er patient
     * @see public void opretBruger(String navn, String email, String password, boolean erBehandler)
     */
    private void opretBrugerService(String navn, String email, String password, boolean erBehandler) {
            String hashedPassword = tekstHasher.hashTekst(password);
            Bruger bruger = new Bruger(navn, email, hashedPassword, erBehandler);
            brugere.add(bruger);
            support.firePropertyChange("opretBruger", null, bruger);
    }

    /**
     * Metoden kaldes, når en personen vil slette sin egen bruger. Passwordet hashes først, fordi det password,
     * der er gemt i brugerobjektet, som hentes fra databasen, allerede er hashed. Når brugeren er slettet sættes
     * aktivBruger til null (brugeren er logged ud). Hvis en behandler vil slette en patients bruger, skal sletPatient-
     * metoden bruges.
     * @param bruger den bruger, der skal slettes
     * @param password det indtastede password
     * @throws ForkertPasswordException hvis det indtastede password ikke matcher brugerens password
     * @see public void sletPatient(Bruger patient, String email)
     */
    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        String hashedPassword = tekstHasher.hashTekst(password);
        String hashedBrugerPassword = bruger.getPassword();

        if (!hashedPassword.equals(hashedBrugerPassword))
            throw new ForkertPasswordException();

        brugere.remove(bruger);
        support.firePropertyChange("sletBruger", null, bruger);

        aktivBruger = null;
        // TODO Anders ville have Bruger til at klare hashing?
        // TODO Husk at håndtere chats, der stadig eksisterer med slettede brugere
    }

    /**
     * Metoden kaldes, når en behandler vil slette en patients Bruger.
     * @param patient brugeren, der skal slettes
     * @param email den indtastede email
     * @throws ForkertEmailException hvis den indtastede email ikke matcher brugerens email
     */
    public void sletPatient(Bruger patient, String email) throws ForkertEmailException {
        if (!email.equals(patient.getEmail()))
            throw new ForkertEmailException();
        brugere.remove(patient);
        support.firePropertyChange("sletBruger", null, patient);
    }

    public Bruger getAktivBruger() {
        return aktivBruger;
    }

    /**
     * Metoden kaldes, når personen har indtastet sine brugeroplysninger. Først hashes passwordet, og derefter sammen-
     * lignes den indtastede email med listen af brugere.
     * @param email den indtastede email
     * @param password det indtastede password
     * @return hvis en bruger findes, som matcher email, og passwordet matcher denne email, returneres true. Ellers
     * returneres false.
     * @throws ForkertPasswordException hvis password ikke matcher den email, som blev fundet
     */
    public boolean logInd(String email, String password) throws ForkertPasswordException {
        for (int i = 0; i < brugere.size(); i++) {
            String hashedPassword = tekstHasher.hashTekst(password);
            if (brugere.get(i).getEmail().equals(email)) {
                if (brugere.get(i).getPassword().equals(hashedPassword)){
                    aktivBruger = brugere.get(i);
                    return true;
                }
                else
                    throw new ForkertPasswordException();
            }
        }
        return false;
    }

    /**
     * Metoden kaldes, når brugeren logger ud af systemet.
     */
    public void logUd() {
        aktivBruger = null;
    }

    /**
     * Metoden kaldes, når systemet skal hente en bruger med et specifikt navn.
     * @param navn navnet på den bruger, man ønsker at finde.
     * @return hvis en bruger med det indtastede navn findes på listen, returneres brugeren. Ellers returneres null.
     */
    public Bruger hentBrugerMedNavn(String navn) {
        for (int i = 0; i < brugere.size(); i++) {
            if (brugere.get(i).getNavn().equals(navn))
                return brugere.get(i);
        }
        return null;
    }

    /**
     * Metoden kaldes, når systemet skal hente en bruger med en specifik email.
     * @param email email på den bruger, man ønsker at finde.
     * @return hvis en bruger med den indtastede email findes på listen, returneres brugeren. Ellers returneres null.
     */
    public Bruger hentBrugerMedEmail(String email) {
        for (int i = 0; i < brugere.size(); i++) {
            if (brugere.get(i).getEmail().equals(email))
                return brugere.get(i);
        }
        return null;
    }

    public void setBrugere(ArrayList<Bruger> brugere) {
        this.brugere = brugere;
    }

    public ArrayList<Bruger> hentBrugere() {
        return brugere;
    }

    /** @author Kelvin */
    public ArrayList<Bruger> hentPatienter() {
        ArrayList<Bruger> patienter = new ArrayList<>();
        if (brugere != null) {
            for (Bruger bruger : brugere) {
                if (!bruger.isErBehandler()) {
                    patienter.add(bruger);
                }
            }
        }
        return patienter;
    }

    /**
     * @author Benjamin
     * Metoden gennemgår listen af brugere og tjekker, om de er behandlere. Hvis de er behandlere tilføjes de til en ny
     * liste af behandlere.
     * @return returnerer listen af behandlere
     */
    public ArrayList<Bruger> hentBehandlere() {
        ArrayList<Bruger> behandlere = new ArrayList<>();
        if (brugere != null) {
            for (Bruger bruger : brugere) {
                if (bruger.isErBehandler()) {
                    behandlere.add(bruger);
                }
            }
        }
        return behandlere;
    }

    public void setAktivBruger(Bruger aktivBruger) {
        this.aktivBruger = aktivBruger;
    }

    /**
     * Metoden bruges i test
     * @return returnerer en ny instans af Validering
     */
    protected Validering newValidering() {
        return new Validering(this);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
}