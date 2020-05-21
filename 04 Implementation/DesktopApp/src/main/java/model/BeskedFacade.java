package model;

import domain.Chat;

import java.io.IOException;

/** @author Benjamin */
public class BeskedFacade {
    private BeskedManager beskedManager;

    public BeskedFacade() throws IOException {
        beskedManager = new BeskedManager();
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return beskedManager.hentChat(afsender, modtager, emne);
    }
}
