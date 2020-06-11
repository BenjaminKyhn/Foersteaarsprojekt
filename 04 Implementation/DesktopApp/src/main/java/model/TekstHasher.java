package model;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Benjamin
 * TekstHasher har kun en enkelt metode med det formål, at hashe tekster med SHA-256-algoritmen. I vores program hashes
 * indtil videre kun passwords.
 */
public class TekstHasher {

    /**
     * Metoden kaldes, når en tekst skal hashes.
     * @param tekst den tekst, som skal hashes.
     * @return en String, som er hashed.
     */
    public String hashTekst(String tekst){
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
}
