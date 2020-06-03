package com.example.android.androidapp.usecases;

import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Chat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeskedManagerTest {
    @Test
    public void hentChatsUT030101() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        MockChat mockChat = new MockChat("test");
        List<Chat> chats = new ArrayList<>();
        chats.add(mockChat);
        beskedManager.setChats(chats);
        assertEquals("test", beskedManager.hentChats().get(0).getEmne());
    }

    @Test
    public void sendBeskedUT030501(){
        BeskedManager beskedManager = new TestbarBeskedManager();
        Chat chat = new Chat("Hans", "Mogens", "testemne");
        String besked = "testbesked";
        beskedManager.sendBesked(besked, chat, "Hans", "Mogens");
        assertEquals("testbesked", chat.getBeskeder().get(0).getBesked());
        assertEquals("Hans", chat.getBeskeder().get(0).getAfsender());
        assertEquals("Mogens", chat.getBeskeder().get(0).getModtager());
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

    private class TestbarBeskedManager extends BeskedManager {

        public TestbarBeskedManager(){
            setChats(new ArrayList<>());
        }
    }
}
