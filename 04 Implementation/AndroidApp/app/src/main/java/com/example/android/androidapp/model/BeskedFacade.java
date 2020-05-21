package com.example.android.androidapp.model;

import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.exceptions.ForMangeTegnException;
import com.example.android.androidapp.model.exceptions.TomBeskedException;
import com.example.android.androidapp.model.exceptions.TomEmneException;

/** @author Tommy **/
public class BeskedFacade {
    private BeskedManager beskedManager = new BeskedManager();
    private Validering validering = new Validering();

    public Chat hentChat(String afsender, String modtager, String emne) {
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public void sendBesked(String besked, Chat chat) {
        beskedManager.sendBesked(besked, chat);
    }

    public void tjekEmne(String emne) throws TomEmneException, ForMangeTegnException {
        validering.tjekEmne(emne);
    }

    public void tjekBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        validering.tjekBesked(besked);
    }
}
