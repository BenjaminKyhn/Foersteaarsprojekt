package unittests.usecases;
import static org.junit.Assert.*;

import entities.Bruger;
import entities.exceptions.*;
import org.junit.Test;

import java.util.ArrayList;
/**@author Tommy**/
public class BrugerManagerTest {
    @Test
    public void opretBrugerUT010401() {
        MockBruger mockBruger = new MockBruger("Johnny", "fas39luck", false);
        BrugerManager brugerManager = new TestbarBrugerManager();
        brugerManager.setAktivBruger(mockBruger);
        String navn = "Hans";
        String email = "hans@email.dk";
        String password = "123";
        assertThrows(BrugerErIkkeBehandlerException.class, () -> brugerManager.opretBruger(navn, email, password, false));
    }

    @Test
    public void opretBrugerUT010402() throws BrugerErIkkeBehandlerException, TomPasswordException, PasswordLaengdeException, TomNavnException, EksisterendeBrugerException, TomEmailException {
        MockBruger mockBruger = new MockBruger("Johnny", "fas39luck", true);
        BrugerManager brugerManager = new TestbarBrugerManager();
        brugerManager.setAktivBruger(mockBruger);
        String navn = "Hans";
        String email = "hans@email.dk";
        String password = "123456";
        boolean erBehandler = false;
        brugerManager.opretBruger(navn, email, password, erBehandler);
        String output = brugerManager.hentBrugere().get(0).getNavn();
        assertEquals("Hans", output);
    }

    @Test
    public void sletBrugerUT020101() throws ForkertPasswordException {
        String test123 = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae";
        MockBruger mockBruger = new MockBruger("Johnny", test123, true);
        BrugerManager brugerManager = new TestbarBrugerManager(mockBruger);
        brugerManager.setAktivBruger(mockBruger);
        assertNotNull(brugerManager.getAktivBruger());
        brugerManager.sletBruger(mockBruger, "test123");
        assertNull(brugerManager.getAktivBruger());
    }

    @Test
    public void sletBrugerUT020102() {
        String test123 = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae";
        MockBruger mockBruger = new MockBruger("Johnny", test123, true);
        BrugerManager brugerManager = new TestbarBrugerManager(mockBruger);
        brugerManager.setAktivBruger(mockBruger);
        assertNotNull(brugerManager.getAktivBruger());
        assertThrows(ForkertPasswordException.class, () -> brugerManager.sletBruger(mockBruger, "test1234"));
    }

    @Test
    public void tilknytBehandlerUT050101() throws ForkertRolleException {
        String test123 = "";
        MockBruger patient = new MockBruger("Stanley Woo", test123, false);
        MockBruger behandler = new MockBruger("Tommy Lee", test123, true);
        BrugerManager brugerManager = new TestbarBrugerManager();
        brugerManager.tilknytBehandler(patient, behandler);
        assertEquals("Tommy Lee", patient.getBehandlere().get(0));

    }

   @Test
   public void tilknytBehandlerUT050102() {
        String test123 = "";
       MockBruger patient = new MockBruger("Flint Westwood", test123, true);
       MockBruger behandler = new MockBruger("Stevie Wonders", test123, false);
       BrugerManager brugerManager = new TestbarBrugerManager();
       assertThrows(ForkertRolleException.class, () -> brugerManager.tilknytBehandler(patient, behandler));
    }

    @Test
    public void tilknytBehandlerUT050103() {
        String test123 = "";
        MockBruger patient = new MockBruger("Warwick Davis", test123, false);
        MockBruger behandler = new MockBruger("Charles Manson", test123, true);
        patient.setBehandlere(null);
        BrugerManager brugerManager = new TestbarBrugerManager();
        assertThrows(NullPointerException.class, () -> brugerManager.tilknytBehandler(patient,behandler));
    }

    private class MockBruger extends Bruger {
        String navn;
        String password;
        boolean erBehandler;

        public MockBruger(String navn, String password, boolean erBehandler){
            this.navn = navn;
            this.password = password;
            this.erBehandler = erBehandler;
            setBehandlere(new ArrayList<>());
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

    private class MockValidering extends Validering{

        @Override
        public void tjekEmail(String email) throws TomEmailException, EksisterendeBrugerException {
        }
    }

    private class TestbarBrugerManager extends BrugerManager {
        public TestbarBrugerManager() {
            setBrugere(new ArrayList<>());
        }

        @Override
        protected Validering newValidering() {
            return new MockValidering();
        }

        public TestbarBrugerManager(MockBruger bruger) {
            ArrayList<Bruger> mockBrugere = new ArrayList<>();
            mockBrugere.add(bruger);
            setBrugere(mockBrugere);
        }
    }
}
