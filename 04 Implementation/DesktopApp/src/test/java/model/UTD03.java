package model;

import entities.Besked;
import entities.Bruger;
import entities.Chat;
import entities.exceptions.BrugerFindesIkkeException;
import entities.exceptions.ForMangeTegnException;
import entities.exceptions.TomBeskedException;
import entities.exceptions.TomEmneException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**@author Benjamin**/
public class UTD03 {
    @Test
    public void hentChatsUT030101() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        MockChat mockChat = new MockChat("test");
        ArrayList<Chat> chats = new ArrayList<>();
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
    public void tjekEmneUT030201() {
        Validering validering = new Validering();
        String emne = null;
        assertThrows(NullPointerException.class, () -> validering.tjekEmne(emne));

    }

    @Test
    public void tjekEmneUT030202() {
        Validering validering = new Validering();
        String emne = "";
        assertThrows(TomEmneException.class, () -> validering.tjekEmne(emne));
    }

    @Test
    public void tjekEmneUT030203() {
        Validering validering = new Validering();
        String emne = "Emne1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        assertThrows(ForMangeTegnException.class, () -> validering.tjekEmne(emne));
    }

    @Test
    public void tjekEmneUT030204() throws TomEmneException, ForMangeTegnException {
        Validering validering = new Validering();
        String emne = "Emne";
        validering.tjekEmne(emne);

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
    public void tjekBeskedUT030401() {
        Validering validering = new Validering();
        String besked = null;
        assertThrows(NullPointerException.class, () -> validering.tjekBesked(besked));
    }

    @Test
    public void tjekBeskedUT030402() {
        Validering validering = new Validering();
        String besked = "";
        assertThrows(TomBeskedException.class, () -> validering.tjekBesked(besked));
    }

    @Test
    public void tjekBeskedUT030403() {
        Validering validering = new Validering();
        String besked = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttestesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttestte";
        assertThrows(ForMangeTegnException.class, () -> validering.tjekBesked(besked));
    }

    @Test
    public void tjekBeskedUT030404() throws TomBeskedException, ForMangeTegnException {
        Validering validering = new Validering();
        String besked = "test";
        validering.tjekBesked(besked);
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

    private class MockBrugerManager extends BrugerManager {
        @Override
        public Bruger getAktivBruger(){
            return new MockBruger("Hans");
        }

        @Override
        public Bruger hentBrugerMedNavn(String navn) {
            if (navn == null) {
                return null;
            }

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

    private class TestbarBeskedManager extends BeskedManager{

        public TestbarBeskedManager(){
            setChats(new ArrayList<>());
        }

        @Override
        protected BrugerManager newBrugerManager() {
            return new MockBrugerManager();
        }
    }
}
