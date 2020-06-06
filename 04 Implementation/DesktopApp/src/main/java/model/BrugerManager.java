package model;

import entities.Bruger;
import entities.exceptions.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin
 */
public class BrugerManager {
    private Bruger aktivBruger;
    private static BrugerManager brugerManager;
    private ArrayList<Bruger> brugere;
    private TekstHasher tekstHasher;
    private Validering validering;
    private PropertyChangeSupport support;

    public BrugerManager() {
        tekstHasher = new TekstHasher();
        validering = newValidering();
        support = new PropertyChangeSupport(this);
    }

    public static synchronized BrugerManager getInstance() {
        if (brugerManager == null) {
            brugerManager = new BrugerManager();
        }
        return brugerManager;
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

    /** @author Benjamin */
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

    private void opretBrugerService(String navn, String email, String password, boolean erBehandler) {
            String hashedPassword = tekstHasher.hashTekst(password);
            Bruger bruger = new Bruger(navn, email, hashedPassword, erBehandler);
            brugere.add(bruger);
            support.firePropertyChange("opretBruger", null, bruger);
    }

    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        String hashedPassword = tekstHasher.hashTekst(password);
        String hashedBrugerPassword = bruger.getPassword();

        if (!hashedPassword.equals(hashedBrugerPassword))
            throw new ForkertPasswordException();

        brugere.remove(bruger);
        support.firePropertyChange("sletBruger", null, bruger);

        aktivBruger = null;
        // TODO Anders ville have Bruger til at klare hashing?
    }

    public Bruger getAktivBruger() {
        return aktivBruger;
    }

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

    public void logUd() {
        aktivBruger = null;
    }

    public Bruger hentBrugerMedNavn(String navn) {
        for (int i = 0; i < brugere.size(); i++) {
            if (brugere.get(i).getNavn().equals(navn))
                return brugere.get(i);
        }
        return null;
    }

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

    /** @author Benjamin */
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

    protected Validering newValidering() {
        return new Validering(this);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
}