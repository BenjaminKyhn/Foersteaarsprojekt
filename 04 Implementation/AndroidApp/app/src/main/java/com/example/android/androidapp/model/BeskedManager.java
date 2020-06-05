package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;

import java.util.List;

/** @author Tommy **/
class BeskedManager {
    private List<Chat> chats;
    private BrugerManager brugerManager;

    public BeskedManager() {
        brugerManager = newBrugerManager();
    }

    public BeskedManager(BrugerManager brugerManager) {
        this.brugerManager = brugerManager;
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

        //TODO Lav tjek, om modtageren eksister. Der skal laves en setter til brugerManager;
    }

    void sendBesked(String besked, Chat chat, String afsender, String modtager) throws TomBeskedException, ForMangeTegnException {
        Validering validering = new Validering();
        validering.tjekBesked(besked);
        long now = System.currentTimeMillis();
        Besked beskedObjekt = new Besked(besked, now, afsender, modtager);
        chat.tilfoejBesked(beskedObjekt);
    }

    void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Chat> hentChats() {
        return chats;
    }

    protected BrugerManager newBrugerManager() {
        return new BrugerManager();
    }
}
