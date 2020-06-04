package unittests.usecases;

import entities.Besked;
import entities.Chat;
import entities.exceptions.BrugerFindesIkkeException;
import entities.exceptions.ForMangeTegnException;
import entities.exceptions.TomBeskedException;
import entities.exceptions.TomEmneException;

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

    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException, TomEmneException, ForMangeTegnException {
        tjekEmne(emne);
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

    public void sendBesked(String besked, Chat chat) throws TomBeskedException, ForMangeTegnException {
        tjekBesked(besked);
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

