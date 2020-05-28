package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.model.exceptions.ForkertPasswordException;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/** @author Tommy **/
class BrugerManager {
    private Bruger aktivBruger;
    private List<Bruger> brugere;

    void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        if (aktivBruger != null) {
            throw new BrugerLoggedeIndException();
        }
        String enkrypteretPassword = enkrypterTekst(password);
        Bruger bruger = new Bruger(navn, email, enkrypteretPassword);
        brugere.add(bruger);
        aktivBruger = bruger;
    }

    void sletBruger(Bruger bruger, String password) throws ForkertPasswordException {
        String enkrypteretPassword = enkrypterTekst(password);
        String enkrypteretBrugerPassword = bruger.getPassword();

        if (!enkrypteretPassword.equals(enkrypteretBrugerPassword)) {
            throw new ForkertPasswordException();
        }
        brugere.remove(bruger);
        aktivBruger = null;
    }

    boolean logInd(String email, String password) {
        boolean loggedeInd = false;
        for (Bruger bruger : brugere) {
            if (bruger.getEmail().equals(email)) {
                if (bruger.getPassword().equals(enkrypterTekst(password))) {
                    aktivBruger = bruger;
                    loggedeInd = true;
                }
            }
        }
        return loggedeInd;
    }

    void logUd() {
        if (aktivBruger != null) {
            aktivBruger = null;
        }
    }

    private String enkrypterTekst(String tekst) {
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

    void setBrugere(List<Bruger> brugere) {
        this.brugere = brugere;
    }

    Bruger getAktivBruger() {
        return aktivBruger;
    }
}
