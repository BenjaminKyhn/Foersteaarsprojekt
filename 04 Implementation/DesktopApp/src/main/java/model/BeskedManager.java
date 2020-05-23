package model;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
import model.exceptions.BrugerFindesIkkeException;
import persistence.DatabaseManager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/** @author Benjamin */
public class BeskedManager {
    private DatabaseManager databaseManager;
    private BrugerManager brugerManager;
    private static BeskedManager beskedManager;

    public BeskedManager() throws IOException {
        databaseManager = DatabaseManager.getInstance();
        brugerManager = BrugerManager.getInstance();
    }

    public static synchronized BeskedManager getInstance() throws IOException {
        if (beskedManager == null){
            beskedManager = new BeskedManager();
        }
        return beskedManager;
    }

    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException {
        Bruger afsender = brugerManager.getAktivBruger();
        Bruger modtager = databaseManager.hentBrugerMedNavn(navn);
        if (modtager == null)
            throw new BrugerFindesIkkeException();
        Chat nyChat = new Chat(afsender.getNavn(), modtager.getNavn(), emne);
        databaseManager.opretChat(nyChat);
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return databaseManager.hentChat(afsender, modtager, emne);
    }

    public ArrayList<Chat> hentChatsMedNavn(String navn){
        return databaseManager.hentChatsMedNavn(navn);
    }

    public ArrayList<Besked> hentBeskeder(Chat chat){
        return chat.getBeskeder();
    }

    public void sendBesked(String besked, Chat chat){
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
        databaseManager.opdaterChat(chat, beskedObjekt);
    }
}
