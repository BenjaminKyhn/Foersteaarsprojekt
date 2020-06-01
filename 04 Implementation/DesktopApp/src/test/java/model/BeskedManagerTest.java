package model;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
import model.exceptions.BrugerFindesIkkeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
    public void opretChatUT030301() throws BrugerFindesIkkeException {
        BeskedManager beskedManager = new TestbarBeskedManager();
        beskedManager.opretChat("Boris", "Skulderskade");
        String output = beskedManager.hentChats().get(0).getModtager();
        assertEquals("Boris", output);
    }

    @Test
    public void opretChatUT030302() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat("Egon", "Skulderskade"));
    }

    @Test
    public void opretChatUT030303() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat(null, "Skulderskade"));
    }

    @Test
    public void sendBeskedUT030501(){
        BeskedManager beskedManager = new TestbarBeskedManager();
        long tidspunkt = 150000000000L;
        Chat chat = new Chat("Hans", "Mogens", "testemne", tidspunkt);
        String besked = "testbesked";
        beskedManager.sendBesked(besked, chat);
        assertEquals("testbesked", chat.getBeskeder().get(0).getBesked());
        assertEquals("Hans", chat.getBeskeder().get(0).getAfsender());
        assertEquals("Mogens", chat.getBeskeder().get(0).getModtager());

        /** Vi kan ikke lave assertEquals på tidspunktet, da metoden sendBesked bruger System.currentTimeMillis(),
         * hvilket betyder, at vi så ville teste på System-klassen */
    }

    @Test
    public void hentBeskederUT040201(){
        BeskedManager beskedManager = new TestbarBeskedManager();
        MockChat mockChat = new MockChat("testemne");
        MockBesked mockBesked = new MockBesked("testbesked");
        mockChat.tilfoejBesked(mockBesked);
        assertEquals("testbesked", beskedManager.hentBeskeder(mockChat).get(0).getBesked());

        /** Er det ok at mocke alting på nær beskedManager.hentBeskeder? */
    }

    private class MockBrugerManager extends BrugerManager {
        @Override
        public Bruger getAktivBruger(){
            return new MockBruger("Hans");
        }

        @Override
        public Bruger hentBrugerMedNavn(String navn) {
            if (!navn.equals("Boris")) {
                return null;
            }
            return new MockBruger(navn);
        }
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

    private class MockChat extends Chat{
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

    /** Constructoren skal overrides, så derfor er det nødvendigt at definere en TestbarBeskedManager */
    private class TestbarBeskedManager extends BeskedManager{

        @Override
        protected BrugerManager newBrugerManager() {
            setChats(new ArrayList<>());
            return new MockBrugerManager();
        }
    }
}
