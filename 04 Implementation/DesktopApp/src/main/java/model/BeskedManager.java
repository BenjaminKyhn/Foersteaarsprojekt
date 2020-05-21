package model;

import domain.Chat;
import persistence.DatabaseManager;

import java.io.IOException;

/** @author Benjamin */
public class BeskedManager {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    public BeskedManager() throws IOException {
    }

    public Chat hentChat(String afsender, String modtager, String emne){
        return databaseManager.hentChat(afsender, modtager, emne);
    }
}
