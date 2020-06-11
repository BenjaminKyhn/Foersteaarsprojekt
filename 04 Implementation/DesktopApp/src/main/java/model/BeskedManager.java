package model;

import entities.Besked;
import entities.Bruger;
import entities.Chat;
import entities.exceptions.BrugerFindesIkkeException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin
 */
public class BeskedManager {
    private BrugerManager brugerManager;
    private static BeskedManager beskedManager;
    private ArrayList<Chat> chats;
    private PropertyChangeSupport support;

    public BeskedManager() {
        brugerManager = newBrugerManager();
        support = new PropertyChangeSupport(this);
    }

    public static synchronized BeskedManager getInstance() {
        if (beskedManager == null) {
            beskedManager = new BeskedManager();
        }
        return beskedManager;
    }

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

    public ArrayList<Chat> hentChats() {
        return chats;
    }

    public ArrayList<Besked> hentBeskeder(Chat chat) {
        return chat.getBeskeder();
    }

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