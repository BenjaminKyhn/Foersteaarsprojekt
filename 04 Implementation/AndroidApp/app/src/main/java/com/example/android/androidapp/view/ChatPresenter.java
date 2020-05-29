package com.example.android.androidapp.view;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Chat;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.exceptions.ForMangeTegnException;
import com.example.android.androidapp.model.exceptions.TomBeskedException;
import com.example.android.androidapp.persistence.DatabaseManager;
import com.example.android.androidapp.util.ObserverbarListe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

class ChatPresenter {
    private Chat chat;
    private String beskedAfsender;
    private String beskedModtager;
    private ObserverbarListe<Besked> beskeder;
    private PropertyChangeSupport support;
    ChatPresenter(String afsender, String modtager, String emne) {
        chat = BeskedFacade.hentInstans().hentChat(afsender, modtager, emne);
        beskeder = chat.getBeskeder();
        support = new PropertyChangeSupport(this);
        observerChat(chat);
    }

    private void observerChat(final Chat chat) {
        chat.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("nyBesked")) {
                    if (!evt.getNewValue().equals(chat.getBeskeder().get(chat.getBeskeder().size() - 1))) {
                        support.firePropertyChange("nyBesked", null, beskeder);
                    }
                }
            }
        });
    }

    public void tilfoejObserver(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    void sendBesked(String besked) throws TomBeskedException, ForMangeTegnException {
        BeskedFacade beskedFacade = BeskedFacade.hentInstans();
        beskedFacade.tjekBesked(besked);
        beskedFacade.sendBesked(besked, chat, beskedAfsender, beskedModtager);
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.opdaterChat(chat);
    }

    public ObserverbarListe<Besked> getBeskeder() {
        return beskeder;
    }

    public Chat getChat() {
        return chat;
    }

    public void setBeskedAfsender(String beskedAfsender) {
        this.beskedAfsender = beskedAfsender;
    }

    public void setBeskedModtager(String beskedModtager) {
        this.beskedModtager = beskedModtager;
    }
}
