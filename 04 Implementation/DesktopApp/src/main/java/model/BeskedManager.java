package model;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
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

    public void opretChat(String navn, String emne){
        Bruger afsender = brugerManager.getAktivBruger();
        Bruger modtager = databaseManager.hentBrugerMedNavn(navn);
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
        Besked beskedObjekt = new Besked(besked);
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        beskedObjekt.setTidspunkt(timestamp);
        chat.tilfoejBesked(beskedObjekt);
        databaseManager.opdaterChat(chat, beskedObjekt);
    }
}
