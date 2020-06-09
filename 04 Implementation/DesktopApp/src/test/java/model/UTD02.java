package model;

import entities.Bruger;
import entities.exceptions.ForkertPasswordException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UTD02 {
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
        public void tjekEmail(String email) {
        }
    }

    private class TestbarBrugerManager extends BrugerManager {
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
