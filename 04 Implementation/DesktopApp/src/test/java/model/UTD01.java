package model;
import static org.junit.Assert.*;

import entities.Bruger;
import entities.exceptions.*;
import org.junit.Test;

import java.util.ArrayList;
/**@author Tommy**/
public class UTD01 {
    @Test
    public void tjekEmailUT010101() {
        Validering validering = new TestbarValidering();
        String email = null;
        assertThrows(NullPointerException.class, () -> validering.tjekEmail(email));
    }

    @Test
    public void tjekEmailUT010102() {
        Validering validering = new TestbarValidering();
        String email = "fys@frbsport.dk";
        assertThrows(EksisterendeBrugerException.class, () -> validering.tjekEmail(email));
    }

    @Test
    public void tjekEmailUT010103() {
        Validering validering = new TestbarValidering();
        String email = "";
        assertThrows(TomEmailException.class, () -> validering.tjekEmail(email));
    }

    @Test
    public void tjekEmailUT010104() throws EksisterendeBrugerException, TomEmailException {
        Validering validering = new TestbarValidering();
        String email = "gertrud57@hotmail.com";
        validering.tjekEmail(email);
    }

    @Test
    public void tjekNavnUT010201() {
        Validering validering = new Validering();
        String navn = null;
        assertThrows(NullPointerException.class, () -> validering.tjekNavn(navn));
    }

    @Test
    public void tjekNavnUT010202() {
        Validering validering = new Validering();
        String navn = "";
        assertThrows(TomNavnException.class, () -> validering.tjekNavn(navn));
    }

    @Test
    public void tjekNavnUT010203() throws TomNavnException {
        Validering validering = new Validering();
        String navn = "Thomas Gustafsson";
        validering.tjekNavn(navn);
    }

    @Test
    public void tjekPasswordUT010301() {
        Validering validering = new Validering();
        String password = null;
        assertThrows(NullPointerException.class, () -> validering.tjekPassword(password));
    }

    @Test
    public void tjekPasswordUT010302() {
        Validering validering = new Validering();
        String password = "";
        assertThrows(TomPasswordException.class, () -> validering.tjekPassword(password));
    }

    @Test
    public void tjekPasswordUT010303() {
        Validering validering = new Validering();
        String password = "12345";
        assertThrows(PasswordLaengdeException.class, () -> validering.tjekPassword(password));
    }

    @Test
    public void tjekPasswordUT010304() {
        Validering validering = new Validering();
        String password = "123456789123456789123";
        assertThrows(PasswordLaengdeException.class, () -> validering.tjekPassword(password));
    }

    @Test
    public void tjekPasswordUT010305() throws Exception {
        Validering validering = new Validering();
        String password = "1234567";
        validering.tjekPassword(password);
    }

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

    private class MockBruger extends Bruger {
        String navn;
        String email;
        String password;
        boolean erBehandler;

        public MockBruger(String navn, String password, boolean erBehandler){
            this.navn = navn;
            this.password = password;
            this.erBehandler = erBehandler;
            setBehandlere(new ArrayList<>());
        }

        public MockBruger(String email) {
            this.email = email;
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

    private class MockBrugerManager extends BrugerManager {
        @Override
        public Bruger hentBrugerMedEmail(String email) {
            if (email.equals("fys@frbsport.dk")) {
                return new MockBruger("fys@frbsport.dk");
            } else {
                return null;
            }
        }
    }

    private class TestbarValidering extends Validering {

        @Override
        protected BrugerManager newBrugerManager() {
            return new MockBrugerManager();
        }
    }
}
