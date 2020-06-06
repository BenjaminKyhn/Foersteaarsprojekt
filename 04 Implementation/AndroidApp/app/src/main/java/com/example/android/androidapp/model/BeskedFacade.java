package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/** @author Tommy **/
public class BeskedFacade {
    private BeskedManager beskedManager;
    private Validering validering;
    private static BeskedFacade beskedFacade;

    private BeskedFacade() {
        beskedManager = new BeskedManager();
        validering = new Validering();
    }

    public static synchronized BeskedFacade hentInstans() {
        if (beskedFacade == null) {
            beskedFacade = new BeskedFacade();
        }
        return beskedFacade;
    }

    public Chat hentChat(String afsender, String modtager, String emne) {
        return beskedManager.hentChat(afsender, modtager, emne);
    }

    public void opretChat(String afsender, String modtager, String emne) throws BrugerFindesIkkeException, TomEmneException, ForMangeTegnException {
        beskedManager.opretChat(afsender, modtager, emne);
    }

    public ArrayList<Chat> hentNuvaerendeListe() {
        return beskedManager.hentChats();
    }

    public void sendBesked(String besked, Chat chat, String afsender, String modtager) throws TomBeskedException, ForMangeTegnException {
        beskedManager.sendBesked(besked, chat, afsender, modtager);
    }

    public void tjekEmne(String emne) throws TomEmneException, ForMangeTegnException {
        validering.tjekEmne(emne);
    }

    public void tjekBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        validering.tjekBesked(besked);
    }

    public void saetListeAfChats(ArrayList<Chat> chats) {
        beskedManager.setChats(chats);
    }

    public void tilfoejListener(PropertyChangeListener listener) {
        beskedManager.tilfoejListener(listener);
    }

    public void fjernListener(PropertyChangeListener listener) {
        beskedManager.fjernListener(listener);
    }

    public void setBrugerManager(BrugerManager brugerManager) {
        beskedManager.setBrugerManager(brugerManager);
    }
}
