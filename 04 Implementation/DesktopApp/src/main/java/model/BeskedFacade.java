package model;

import domain.Chat;
import model.exceptions.ForMangeTegnException;
import model.exceptions.TomBeskedException;
import model.exceptions.TomEmneException;

import java.io.IOException;

/** @author Benjamin */
public class BeskedFacade {
    private BeskedManager beskedManager;
    private Validering validering;

    public BeskedFacade() throws IOException {
        beskedManager = new BeskedManager();
        validering = new Validering();
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public void sendBesked(String besked, Chat chat){
        beskedManager.sendBesked(besked, chat);
    }

    public void tjekEmne(String emne) throws TomEmneException, ForMangeTegnException {
        validering.tjekEmne(emne);
    }

    public void tjekBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        validering.tjekBesked(besked);
    }
}
