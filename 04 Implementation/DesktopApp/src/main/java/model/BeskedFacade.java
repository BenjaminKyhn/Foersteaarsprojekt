package model;

import domain.Besked;
import domain.Chat;
import model.exceptions.ForMangeTegnException;
import model.exceptions.TomBeskedException;
import model.exceptions.TomEmneException;

import java.io.IOException;
import java.util.ArrayList;

/** @author Benjamin */
public class BeskedFacade {
    private BeskedManager beskedManager;
    private Validering validering;
    private static BeskedFacade beskedFacade;

    public BeskedFacade() throws IOException {
        beskedManager = new BeskedManager();
        validering = new Validering();
    }

    public static synchronized BeskedFacade getInstance() throws IOException {
        if (beskedFacade == null){
            beskedFacade = new BeskedFacade();
        }
        return beskedFacade;
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public ArrayList<Besked> hentBeskeder(Chat chat){
        return beskedManager.hentBeskeder(chat);
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
