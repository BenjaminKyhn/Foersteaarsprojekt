package model;

import domain.Besked;
import domain.Chat;
import model.exceptions.BrugerFindesIkkeException;
import model.exceptions.ForMangeTegnException;
import model.exceptions.TomBeskedException;
import model.exceptions.TomEmneException;

import java.util.ArrayList;
import java.util.List;

/** @author Benjamin */
public class BeskedFacade {
    private BeskedManager beskedManager;
    private Validering validering;
    private static BeskedFacade beskedFacade;

    public BeskedFacade() {
        beskedManager = BeskedManager.getInstance();
        validering = new Validering();
    }

    public static synchronized BeskedFacade getInstance() {
        if (beskedFacade == null){
            beskedFacade = new BeskedFacade();
        }
        return beskedFacade;
    }

    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException {
        beskedManager.opretChat(navn, emne);
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public List<Chat> hentChats(){
        return beskedManager.hentChats();
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

    public void setChats(List<Chat> chats){
        beskedManager.setChats(chats);
    }
}