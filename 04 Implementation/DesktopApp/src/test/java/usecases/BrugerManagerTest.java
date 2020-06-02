package usecases;
import static org.junit.Assert.*;

import entities.Bruger;
import entities.exceptions.BrugerErIkkeBehandlerException;
import entities.exceptions.ForkertPasswordException;
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
        assertThrows(BrugerErIkkeBehandlerException.class, () -> brugerManager.opretBruger(navn, email, password, false));
    }

    @Test
    public void UT010402() throws BrugerErIkkeBehandlerException {
        MockBruger mockBruger = new MockBruger("Johnny", "fas39luck", true);
        BrugerManager brugerManager = new TestbarBrugerManager();
        brugerManager.setAktivBruger(mockBruger);
        String navn = "Hans";
        String email = "hans@email.dk";
        String password = "123";
        boolean erBehandler = false;
        brugerManager.opretBruger(navn, email, password, erBehandler);
        String output = brugerManager.hentBrugere().get(0).getNavn();
        assertEquals("Hans", output);
    }

    @Test
    public void UT020101() throws ForkertPasswordException {
        String test123 = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae";
        MockBruger mockBruger = new MockBruger("Johnny", test123, true);
        BrugerManager brugerManager = new TestbarBrugerManager(mockBruger);
        brugerManager.setAktivBruger(mockBruger);
        assertNotNull(brugerManager.getAktivBruger());
        brugerManager.sletBruger(mockBruger, "test123");
        assertNull(brugerManager.getAktivBruger());
    }

    @Test
    public void UT020102() {
        String test123 = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae";
        MockBruger mockBruger = new MockBruger("Johnny", test123, true);
        BrugerManager brugerManager = new TestbarBrugerManager(mockBruger);
        brugerManager.setAktivBruger(mockBruger);
        assertNotNull(brugerManager.getAktivBruger());
        assertThrows(ForkertPasswordException.class, () -> brugerManager.sletBruger(mockBruger, "test1234"));
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
            return password;
        }

        @Override
        public boolean isErBehandler() {
            return erBehandler;
        }
    }

    private class TestbarBrugerManager extends BrugerManager {
        public TestbarBrugerManager() {
            setBrugere(new ArrayList<>());
        }

        public TestbarBrugerManager(MockBruger bruger) {
            ArrayList<Bruger> mockBrugere = new ArrayList<>();
            mockBrugere.add(bruger);
            setBrugere(mockBrugere);
        }
    }
}
