package model;

import entities.Besked;
import entities.Chat;
import entities.exceptions.BrugerFindesIkkeException;
import entities.exceptions.ForMangeTegnException;
import entities.exceptions.TomBeskedException;
import entities.exceptions.TomEmneException;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author Benjamin
 * BeskedFacade fungerer som et samlet koblingspunkt for UI-elementer, der vil ændre på beskedelementer. Dens primære
 * funktion er at sende metodekald videre til enten Validering eller BeskedManager.
 */
public class BeskedFacade {
    private BeskedManager beskedManager;
    private Validering validering;
    private static BeskedFacade beskedFacade;

    /**
     * Denne constructor er private, fordi vi anvender singleton pattern.
     */
    private BeskedFacade() {
        beskedManager = BeskedManager.getInstance();
        validering = new Validering();
    }

    /**
     * Når man skal bruge BeskedFacade kaldes denne metode for at sikre, at der aldrig findes mere end én BeskedFacade.
     * @return  returnerer sit statiske variabel beskedFacade
     */
    public static synchronized BeskedFacade getInstance() {
        if (beskedFacade == null){
            beskedFacade = new BeskedFacade();
        }
        return beskedFacade;
    }

    public void rydObservere() {
        beskedManager.rydObservere();
    }

    /**
     * Kaldes, når der skal skabes en ny chat.
     * @param navn modtagerens navn
     * @param emne emne for chatten
     * @throws BrugerFindesIkkeException hvis modtageren ikke findes
     * @throws TomEmneException hvis emnet er en tom String
     * @throws ForMangeTegnException hvis emnet har for mange tegn
     */
    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException, TomEmneException, ForMangeTegnException {
        tjekEmne(emne);
        beskedManager.opretChat(navn, emne);
    }

    public ArrayList<Chat> hentChats(){
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

    public void setChats(ArrayList<Chat> chats){
        beskedManager.setChats(chats);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        beskedManager.tilfoejObserver(listener);
    }
}

