package com.example.android.androidapp.model;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** @author Tommy */
public class TekstHasher {
    public static String hashTekst(String tekst) {
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
