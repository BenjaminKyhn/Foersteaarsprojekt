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
// Beskedmanageren er den egentlige klasse der er ekspert i håndtere beskeder. Den indeholder en samling af chats.
class BeskedManager {
    private ArrayList<Chat> chats;
    private BrugerManager brugerManager;
    private PropertyChangeSupport support;

    public BeskedManager() {
        brugerManager = newBrugerManager();
        support = new PropertyChangeSupport(this);
        chats = new ArrayList<>();
    }

    void rydObservere() {
        support = new PropertyChangeSupport(this);
    }

    /* Da listen af chats er usorteret så må vi iterere igennem den hele indtil vi finder den søgte chat, hvilket i værste tilfælde kan betyder at den .
    * går igennem hele listen inden den finder chatten. */
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

        // brugerManager finder brugeren ved navn og kaster en exception hvis den ikke kan finde en.
        Bruger hentedeBruger = brugerManager.hentBrugerMedNavn(modtager);
        if (hentedeBruger == null)
            throw new BrugerFindesIkkeException();
        Chat chat = new Chat(afsender, hentedeBruger.getNavn(), emne);
        chats.add(chat);

        // Observerkald så tilmeldte listeners for besked.
        support.firePropertyChange("opretChat", null, chat);
    }

    void sendBesked(String besked, Chat chat, String afsender, String modtager) throws TomBeskedException, ForMangeTegnException {
        Validering validering = new Validering();
        validering.tjekBesked(besked);

        // Tidspunktet i beskeden er unix time og så kan denne formateres et andet sted.
        long tidspunkt = System.currentTimeMillis();
        Besked beskedObjekt = new Besked(besked, tidspunkt, afsender, modtager);
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
