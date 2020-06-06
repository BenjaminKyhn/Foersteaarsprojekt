package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/** @author Tommy **/
class BeskedManager {
    private ArrayList<Chat> chats;
    private BrugerManager brugerManager;
    private PropertyChangeSupport support;

    public BeskedManager() {
        brugerManager = newBrugerManager();
        support = new PropertyChangeSupport(this);
        chats = new ArrayList<>();
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

    void opretChat(String afsender, String modtager, String emne) throws BrugerFindesIkkeException, TomEmneException, ForMangeTegnException {
        Validering validering = new Validering();
        validering.tjekEmne(emne);

        Bruger hentedeBruger = brugerManager.hentBrugerMedNavn(modtager);
        if (hentedeBruger == null)
            throw new BrugerFindesIkkeException();
        Chat chat = new Chat(afsender, hentedeBruger.getNavn(), emne);
        chats.add(chat);
        support.firePropertyChange("opretChat", null, chat);
    }

    void sendBesked(String besked, Chat chat, String afsender, String modtager) throws TomBeskedException, ForMangeTegnException {
        Validering validering = new Validering();
        validering.tjekBesked(besked);
        long now = System.currentTimeMillis();
        Besked beskedObjekt = new Besked(besked, now, afsender, modtager);
        chat.tilfoejBesked(beskedObjekt);
    }

    void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    ArrayList<Chat> hentChats() {
        return chats;
    }

    void tilfoejListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    void fjernListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    protected BrugerManager newBrugerManager() {
        return new BrugerManager();
    }

    void setBrugerManager(BrugerManager brugerManager) {
        this.brugerManager = brugerManager;
    }
}
