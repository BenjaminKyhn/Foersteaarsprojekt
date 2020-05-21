package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.persistence.DatabaseManager;

/** @author Tommy **/
class BeskedManager {
    private DatabaseManager databaseManager = new DatabaseManager();

    Chat hentChat(String afsender, String modtager, String emne) {
        return databaseManager.hentChat(afsender, modtager, emne);
    }

    void sendBesked(String besked, Chat chat) {
        Besked beskedObjekt = new Besked();
        beskedObjekt.setBesked(besked);
        beskedObjekt.setTidspunkt("test");
        chat.tilfoejBesked(beskedObjekt);
        databaseManager.opdaterChat(chat);
    }
}
