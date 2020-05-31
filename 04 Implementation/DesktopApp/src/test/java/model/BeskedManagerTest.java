package model;

import domain.Bruger;
import model.exceptions.BrugerFindesIkkeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

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


    private class MockBrugerManager extends BrugerManager {
        public Bruger getAktivBruger(){
            return new MockBruger();
        }

        @Override
        public Bruger hentBrugerMedNavn(String navn) {
            if (navn != "Boris")
                return null;
            return new MockBruger(navn);
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
            setChats(new ArrayList<>());
            return new MockBrugerManager();
        }
    }
}
