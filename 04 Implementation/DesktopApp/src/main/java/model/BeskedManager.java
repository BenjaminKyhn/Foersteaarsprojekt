package model;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
import model.exceptions.BrugerFindesIkkeException;
import persistence.DatabaseManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin
 */
public class BeskedManager {
    private DatabaseManager databaseManager;
    private BrugerManager brugerManager;
    private static BeskedManager beskedManager;
    private List<Chat> chats;

    public BeskedManager() {
        databaseManager = newDatabaseManager();
        brugerManager = newBrugerManager();
    }

    public static synchronized BeskedManager getInstance() {
        if (beskedManager == null) {
            beskedManager = new BeskedManager();
        }
        return beskedManager;
    }

    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException {
        Bruger afsender = brugerManager.getAktivBruger();
        Bruger modtager = databaseManager.hentBrugerMedNavn(navn); /** brugermanager kommer til at holde en liste, som vi kan bruge til at finde modtager */
        String sidstAktiv = new Timestamp(System.currentTimeMillis()).toString();
        if (modtager == null)
            throw new BrugerFindesIkkeException();
        Chat nyChat = new Chat(afsender.getNavn(), modtager.getNavn(), emne, sidstAktiv);
        chats.add(nyChat);
    }

    public Chat hentChat(String afsender, String modtager, String emne) {
        for (int i = 0; i < chats.size(); i++) {
            if (afsender.equals(chats.get(i).getAfsender()))
                if (modtager.equals(chats.get(i).getModtager()))
                    if (emne.equals(chats.get(i).getEmne()))
                        return chats.get(i);
        }
        return null;
    }

    public List<Chat> hentChats() {
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
        String tidspunkt = new Timestamp(System.currentTimeMillis()).toString();
        Besked beskedObjekt = new Besked(afsender, modtager, besked, tidspunkt);
        chat.tilfoejBesked(beskedObjekt);
        chat.setSidstAktiv(tidspunkt);
    }

    protected DatabaseManager newDatabaseManager() {
        return DatabaseManager.getInstance();
    }

    protected BrugerManager newBrugerManager() {
        return BrugerManager.getInstance();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}