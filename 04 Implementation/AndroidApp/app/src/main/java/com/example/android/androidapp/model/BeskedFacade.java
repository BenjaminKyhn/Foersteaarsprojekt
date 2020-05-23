package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.exceptions.ForMangeTegnException;
import com.example.android.androidapp.model.exceptions.TomBeskedException;
import com.example.android.androidapp.model.exceptions.TomEmneException;

import java.util.List;

/** @author Tommy **/
public class BeskedFacade {
    private BeskedManager beskedManager;
    private Validering validering;

    public BeskedFacade() {
        this.beskedManager = new BeskedManager();
        validering = new Validering();
    }

    public Chat hentChat(String afsender, String modtager, String emne) {
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public void sendBesked(String besked, Chat chat, String afsender, String modtager) {
        beskedManager.sendBesked(besked, chat, afsender, modtager);
    }

    public void tjekEmne(String emne) throws TomEmneException, ForMangeTegnException {
        validering.tjekEmne(emne);
    }

    public void tjekBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        validering.tjekBesked(besked);
    }

    public void saetListeAfChats(List<Chat> chats) {
        beskedManager.setChats(chats);
    }
}
