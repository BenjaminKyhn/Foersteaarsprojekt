package model;

import entities.Besked;
import entities.Bruger;
import entities.Chat;
import entities.exceptions.BrugerFindesIkkeException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * @author Benjamin
 * BeskedManager håndterer skabelse og slettelse af chat- og beskedobjekter.
 */
public class BeskedManager {
    private BrugerManager brugerManager;
    private static BeskedManager beskedManager;
    private ArrayList<Chat> chats;
    private PropertyChangeSupport support;

    /**
     * Denne constructor er private, fordi vi anvender singleton pattern.
     */
    BeskedManager() {
        brugerManager = newBrugerManager();
        support = new PropertyChangeSupport(this);
    }

    /**
     * Når man skal bruge BeskedManager kaldes denne metode for at sikre, at der aldrig findes mere end én BeskedManager.
     * @return  returnerer sit statiske variabel beskedManager
     */
    public static synchronized BeskedManager getInstance() {
        if (beskedManager == null) {
            beskedManager = new BeskedManager();
        }
        return beskedManager;
    }

    /**
     * Kaldes, når der skal oprettes en ny chat.
     * @param navn modtagerens navn
     * @param emne emnets indhold
     * @throws BrugerFindesIkkeException når modtageren ikke findes af brugerManager
     */
    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException {
        Bruger afsender = brugerManager.getAktivBruger();
        Bruger modtager = brugerManager.hentBrugerMedNavn(navn);
        long sidstAktiv = System.currentTimeMillis();
        if (modtager == null)
            throw new BrugerFindesIkkeException();
        Chat nyChat = new Chat(afsender.getNavn(), modtager.getNavn(), emne, sidstAktiv);
        chats.add(nyChat);
        support.firePropertyChange("opretChat", null, nyChat);
    }

    /**
     * Getter til BeskedManagers liste af chats
     * @return en liste af alle chats i systemet
     */
    public ArrayList<Chat> hentChats() {
        return chats;
    }

    /**
     * Getter til den beskedliste, som findes i en specifik chat.
     * @param chat den chat, du ønsker at hente beskeder fra
     * @return en liste af alle chattens beskeder
     */
    public ArrayList<Besked> hentBeskeder(Chat chat) {
        return chat.getBeskeder();
    }

    /**
     * Kaldes, når der sendes en ny besked. Metoden skaber et ny beskedobjekt med aktivBruger som afsender.
     * Derefter tjekker den, hvem der er afsender og modtager på chatten. Modtageren af beskeden må være den modsatte af
     * aktivBruger. Metoden tilføjer beskeden til et chatobjekt og opdaterer sidstAktiv.
     * @param besked indholdet af den besked, som skal skabes.
     * @param chat chatten, som skal indeholde beskeden.
     */
    public void sendBesked(String besked, Chat chat) {
        /** Sæt afsender og modtager for beskedobjektet */
        String afsender = brugerManager.getAktivBruger().getNavn();
        String modtager;
        if (afsender.equals(chat.getAfsender()))
            modtager = chat.getModtager();
        else
            modtager = chat.getAfsender();

        /** Lav et beskedobjekt og tilføj det til chatobjektet */
        long tidspunkt = System.currentTimeMillis();
        Besked beskedObjekt = new Besked(afsender, modtager, besked, tidspunkt);
        chat.tilfoejBesked(beskedObjekt);
        chat.setSidstAktiv(tidspunkt);
    }

    /**
     * Denne metode bruges til at skabe en MockBrugerManager i test.
     * @return kalder BrugerManagers constructor.
     */
    protected BrugerManager newBrugerManager() {
        return BrugerManager.getInstance();
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
}