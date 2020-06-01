package model;
import static org.junit.Assert.*;

import domain.Bruger;
import model.exceptions.BrugerErIkkeBehandlerException;
import org.junit.Test;

import java.util.ArrayList;

public class BrugerManagerTest {
    @Test
    public void UT010401() {
        MockBruger mockBruger = new MockBruger("Johnny", "fas39luck", false);
        BrugerManager brugerManager = new TestbarBrugerManager();
        brugerManager.setAktivBruger(mockBruger);
        String navn = "Hans";
        String email = "hans@email.dk";
        String password = "123";
        assertThrows(BrugerErIkkeBehandlerException.class, () -> brugerManager.opretBruger(navn, email, password));
    }

    @Test
    public void UT010402() throws BrugerErIkkeBehandlerException {
        MockBruger mockBruger = new MockBruger("Johnny", "fas39luck", true);
        BrugerManager brugerManager = new TestbarBrugerManager();
        brugerManager.setAktivBruger(mockBruger);
        String navn = "Hans";
        String email = "hans@email.dk";
        String password = "123";
        brugerManager.opretBruger(navn, email, password);
        String output = brugerManager.hentBrugere().get(0).getNavn();
        assertEquals("Hans", output);
    }

    private class MockBruger extends Bruger {
        String navn;
        String password;
        boolean erBehandler;

        public MockBruger(String navn, String password, boolean erBehandler){
            this.navn = navn;
            this.password = password;
            this.erBehandler = erBehandler;
        }

        @Override
        public String getNavn() {
            return navn;
        }

        @Override
        public String getPassword() {
            return super.getPassword();
        }

        @Override
        public boolean isErBehandler() {
            return erBehandler;
        }
    }

    private class TestbarBrugerManager extends BrugerManager {
        MockBruger aktivBruger;

        public TestbarBrugerManager() {
            setBrugere(new ArrayList<>());
        }

        @Override
        public Bruger getAktivBruger() {
            return aktivBruger;
        }
    }
}
