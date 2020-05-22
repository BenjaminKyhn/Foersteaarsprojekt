package model;

import domain.Bruger;
import model.exceptions.BrugerLoggedIndException;
import model.exceptions.ForkertPasswordException;
import org.apache.commons.codec.binary.Hex;
import persistence.DatabaseManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** @author Benjamin */
public class BrugerManager {
    private DatabaseManager databaseManager;
    private Bruger aktivBruger;

    public BrugerManager() throws IOException {
        /** Der kan ikke være 2 instanser af DatabaseManager, så derfor bruger vi getInstance() */
        databaseManager = DatabaseManager.getInstance();
    }

    public void opretBruger(String navn, String email, String password) throws BrugerLoggedIndException {
        if (aktivBruger != null){
            throw new BrugerLoggedIndException();
        }

        String enkrypteretPassword = enkrypterTekst(password);
        Bruger bruger = new Bruger(navn, email, enkrypteretPassword);
        databaseManager.gemBruger(bruger);
        aktivBruger = bruger;
    }

    public void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        String enkrypteretPassword = enkrypterTekst(password);
        String enkrypteretBrugerPassword = bruger.getPassword();

        if (!enkrypteretPassword.equals(enkrypteretBrugerPassword))
            throw new ForkertPasswordException();

        databaseManager.sletBruger(bruger);

        aktivBruger = null;
    }

    public String enkrypterTekst(String tekst){
        String sha256hex = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = tekst.getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(bytes);
            sha256hex = new String(Hex.encodeHex(hash));
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return sha256hex;
    }

    public Bruger getAktivBruger() {
        return aktivBruger;
    }
}
