package model;

import entities.Besked;
import entities.Chat;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UTD04 {
    @Test
    public void hentBeskederUT040201(){
        BeskedManager beskedManager = new TestbarBeskedManager();
        MockChat mockChat = new MockChat("testemne");
        MockBesked mockBesked = new MockBesked("testbesked");
        mockChat.tilfoejBesked(mockBesked);
        assertEquals("testbesked", beskedManager.hentBeskeder(mockChat).get(0).getBesked());
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
        public TestbarBeskedManager(){
            setChats(new ArrayList<>());
        }
    }
}
