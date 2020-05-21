package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Chat;

/** @author Tommy **/
public class BeskedFacade {
    private BeskedManager beskedManager;

    public Chat hentChat(String afsender, String modtager, String emne) {
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public void sendBesked(String besked, Chat chat) {
        beskedManager.sendBesked(besked, chat);
    }
}
