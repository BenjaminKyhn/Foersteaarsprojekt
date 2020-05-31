package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Chat;

import java.sql.Timestamp;
import java.util.List;

/** @author Tommy **/
class BeskedManager {
    private List<Chat> chats;

    Chat hentChat(String[] deltagere, String emne) {
        for (Chat chat : chats) {
            if (chat.getDeltagere()[0].equals(deltagere[0])) {
                if (chat.getDeltagere()[1].equals(deltagere[1])) {
                    if (chat.getEmne().equals(emne)) {
                        return chat;
                    }
                }
            }
        }
        return null;
    }

    private void opretChat(String afsender, String modtager, String emne) {
        String[] deltagere = {afsender, modtager};
        Chat chat = new Chat(deltagere, emne);
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
