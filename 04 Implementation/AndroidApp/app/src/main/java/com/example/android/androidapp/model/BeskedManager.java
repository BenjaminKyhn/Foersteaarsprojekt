package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Chat;

import java.sql.Timestamp;
import java.util.List;

/** @author Tommy **/
class BeskedManager {
    private List<Chat> chats;

    BeskedManager(List<Chat> chats) {
        this.chats = chats;
    }

    Chat hentChat(String afsender, String modtager, String emne) {
        for (Chat chat : chats) {
            if (chat.getAfsender().equals(afsender)) {
                if (chat.getModtager().equals(modtager)) {
                    if (chat.getEmne().equals(emne)) {
                        return chat;
                    }
                }
            }
        }
        return null;
    }

    void sendBesked(String besked, Chat chat) {
        Besked beskedObjekt = new Besked();
        beskedObjekt.setBesked(besked);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        beskedObjekt.setTidspunkt(now.toString());
        chat.tilfoejBesked(beskedObjekt);
    }
}
