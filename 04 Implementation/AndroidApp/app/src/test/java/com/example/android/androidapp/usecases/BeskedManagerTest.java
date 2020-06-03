package com.example.android.androidapp.usecases;

import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeskedManagerTest {
    @Test
    public void opretChatUT030302() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat("Egon", "Kurt", "Skulderskade"));
    }

    private class MockBruger extends Bruger {
        String navn;

        public MockBruger(String navn){
            this.navn = navn;
        }

        @Override
        public String getNavn() {
            return navn;
        }
    }

    private class MockChat extends Chat {
        String emne;
        ArrayList<Besked> beskeder;

        public MockChat(String emne){
            this.emne = emne;
            beskeder = new ArrayList<>();
        }

        @Override
        public String getEmne() {
            return emne;
        }

        @Override
        public void tilfoejBesked(Besked besked){
            beskeder.add(besked);
        }

        @Override
        public ArrayList<Besked> getBeskeder(){
            return beskeder;
        }
    }

    private class MockBesked extends Besked{
        String besked;

        public MockBesked(String besked){
            this.besked = besked;
        }

        public String getBesked(){
            return besked;
        }
    }

    private class TestbarBeskedManager extends BeskedManager{

        @Override
        protected BrugerManager newBrugerManager() {
            setChats(new ArrayList<>());
            return new MockBrugerManager();
        }
    }
}
