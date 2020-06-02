package usecases;

import entities.Bruger;
import entities.exceptions.BrugerFindesIkkeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
/**@author Benjamin**/
public class BeskedManagerTest {
    @Test
    public void opretChatUT010101() throws BrugerFindesIkkeException {
        BeskedManager beskedManager = new TestbarBeskedManager();
        beskedManager.opretChat("Boris", "Skulderskade");
        String output = beskedManager.hentChats().get(0).getModtager();
        assertEquals("Boris", output);
    }

    @Test
    public void opretChatUT010102() {
        BeskedManager beskedManager = new TestbarBeskedManager();
        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat("Egon", "Skulderskade"));
    }

//    @Test
//    public void opretChatUT010103() {
//        BeskedManager beskedManager = new TestbarBeskedManager();
//        assertThrows(BrugerFindesIkkeException.class, () -> beskedManager.opretChat(null, "Skulderskade"));
//    }


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

    private class TestbarBeskedManager extends BeskedManager{

        @Override
        protected BrugerManager newBrugerManager() {
            setChats(new ArrayList<>());
            return new MockBrugerManager();
        }
    }
}
