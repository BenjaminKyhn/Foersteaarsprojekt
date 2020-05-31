package model;

import domain.Bruger;
import model.exceptions.BrugerLoggedIndException;
import model.exceptions.ForkertPasswordException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author Benjamin
 */
public class BrugerManager {
    private Bruger aktivBruger;
    private static BrugerManager brugerManager;
    private List<Bruger> brugere;

    public BrugerManager() {
    }

    public static synchronized BrugerManager getInstance() {
        if (brugerManager == null) {
            brugerManager = new BrugerManager();
        }
        return brugerManager;
    }

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedIndException {
        if (aktivBruger != null) {
            throw new BrugerLoggedIndException();
        }

        String enkrypteretPassword = enkrypterTekst(password);
        Bruger bruger = new Bruger(navn, email, enkrypteretPassword, true);
        brugere.add(bruger);
    }

    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        String enkrypteretPassword = enkrypterTekst(password);
        String enkrypteretBrugerPassword = bruger.getPassword();

        if (!enkrypteretPassword.equals(enkrypteretBrugerPassword))
            throw new ForkertPasswordException();

        brugere.remove(bruger);

        aktivBruger = null;
    }

    //TODO Opret TextHasher-klasse
    public String enkrypterTekst(String tekst) {
        String sha256hex = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = tekst.getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(bytes);
            sha256hex = new String(Hex.encodeHex(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha256hex;
    }

    public Bruger getAktivBruger() {
        return aktivBruger;
    }

    public boolean logInd(String email, String password) throws ForkertPasswordException {
        for (int i = 0; i < brugere.size(); i++) {
            String enkrypteretPassword = enkrypterTekst(password);
            if (brugere.get(i).getEmail().equals(email)) {
                if (brugere.get(i).getPassword().equals(enkrypteretPassword)){
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

    public void setBrugere(List<Bruger> brugere) {
        this.brugere = brugere;
    }

    public List<Bruger> hentBrugere() {
        return brugere;
    }
}