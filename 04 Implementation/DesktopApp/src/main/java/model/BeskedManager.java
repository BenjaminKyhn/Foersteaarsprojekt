package model;

import domain.Besked;
import domain.Chat;
import persistence.DatabaseManager;

import java.io.IOException;
import java.util.ArrayList;

/** @author Benjamin */
public class BeskedManager {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    public BeskedManager() throws IOException {
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return databaseManager.hentChat(afsender, modtager, emne);
    }

    public ArrayList<Besked> hentBeskeder(Chat chat){
        return chat.getBeskeder();
    }

    public void sendBesked(String besked, Chat chat){
        Besked beskedObjekt = new Besked(besked);
        chat.tilfoejBesked(beskedObjekt);
        databaseManager.opdaterChat(chat, beskedObjekt);
    }
}
