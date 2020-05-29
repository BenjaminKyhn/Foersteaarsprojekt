package model;

import domain.Bruger;
import domain.Chat;
import model.exceptions.BrugerFindesIkkeException;
import org.junit.Test;
import static org.junit.Assert.*;
import persistence.DatabaseManager;

public class BeskedManagerTest {
    @Test
    public void opretChatUT010101() throws BrugerFindesIkkeException {
        BeskedManager beskedManager = new TestbarBeskedManager();
        beskedManager.opretChat("Boris", "Skulderskade");
    }

    @Test
    public void opretChatUT010102() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat("Egon", "Skulderskade"));
    }

    @Test
    public void opretChatUT010103() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat(null, "Skulderskade"));
    }

    private class MockDatabaseManager extends DatabaseManager {
        @Override
        public void opretChat(Chat chat){
            System.out.println("Chat er oprettet");
        }

        @Override
        public Bruger hentBrugerMedNavn(String navn){
            if (navn != "Boris")
                return null;
            return new MockBruger(navn);
        }

        public MockDatabaseManager() {
            super(false);
        }
    }

    private class MockBrugerManager extends BrugerManager {
        public Bruger getAktivBruger(){
            return new MockBruger();
        }
    }

    private class MockBruger extends Bruger{
        String navn;

        public MockBruger(){
        }

        public MockBruger(String navn){
            this.navn = navn;
        }
    }

    private class TestbarBeskedManager extends BeskedManager{
        @Override
        protected BrugerManager newBrugerManager() {
            return new MockBrugerManager();
        }
    }
}
