package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Chat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** @author Tommy **/
class BeskedManager {
    private List<Chat> chats;

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

    void opretChat(String afsender, String modtager, String emne) {
        Chat chat = new Chat(afsender, modtager, emne);
        chats.add(chat);
    }

    void sendBesked(String besked, Chat chat, String afsender, String modtager) {
        long now = System.currentTimeMillis();
        Besked beskedObjekt = new Besked(besked, now, afsender, modtager);
        chat.tilfoejBesked(beskedObjekt);
    }

    void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Chat> getChats() {
        return chats;
    }
}
