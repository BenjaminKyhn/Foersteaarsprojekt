package model;

import entities.Bruger;
import entities.exceptions.BehandlerFindesAlleredeException;
import entities.exceptions.ForkertRolleException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UTD05 {
    @Test
    public void tilknytBehandlerUT050101() throws ForkertRolleException, BehandlerFindesAlleredeException {
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
        public void tjekEmail(String email) {
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
    }
}
