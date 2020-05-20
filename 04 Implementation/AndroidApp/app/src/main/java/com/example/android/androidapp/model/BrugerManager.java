package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.model.exceptions.BrugerLoggedeIndException;
import com.example.android.androidapp.persistence.DatabaseManager;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** @author Tommy **/
class BrugerManager {
    private Bruger aktivBruger;
    private DatabaseManager databaseManager = new DatabaseManager();

    void opretBruger(String navn, String email, String password) throws BrugerLoggedeIndException {
        if (aktivBruger != null) {
            throw new BrugerLoggedeIndException();
        }
        String enkrypteretPassword = enkrypterTekst(password);
        Bruger bruger = new Bruger(navn, email, enkrypteretPassword);
        databaseManager.gemBruger(bruger);
        aktivBruger = bruger;
    }

    private String enkrypterTekst(String tekst) {
        String sha256hex = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] bytes = tekst.getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(bytes);
            sha256hex = new String(Hex.encodeHex(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha256hex;
    }
}
