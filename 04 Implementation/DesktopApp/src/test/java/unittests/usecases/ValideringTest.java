package unittests.usecases;

import entities.Bruger;
import entities.exceptions.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Kelvin
 **/
public class ValideringTest {
    @Test
    public void tjekEmailUT010101() {
        Validering validering = new Validering();
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
        Validering validering = new Validering();
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
    public void tjekEmneUT030301() {
        Validering validering = new Validering();
        String emne = null;
        assertThrows(NullPointerException.class, () -> validering.tjekEmne(emne));

    }

    @Test
    public void tjekEmneUT030302() {
        Validering validering = new Validering();
        String emne = "";
        assertThrows(TomEmneException.class, () -> validering.tjekEmne(emne));
    }

    @Test
    public void tjekEmneUT030303() {
        Validering validering = new Validering();
        String emne = "Emne1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        assertThrows(ForMangeTegnException.class, () -> validering.tjekEmne(emne));
    }

    @Test
    public void tjekEmneUT030304() throws TomEmneException, ForMangeTegnException {
        Validering validering = new Validering();
        String emne = "Emne";
        validering.tjekEmne(emne);

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

    private class MockBruger extends Bruger {
        String email;

        public MockBruger(String email) {
            this.email = email;
        }

        @Override
        public String getEmail() {
            return email;
        }
    }

    private class TestbarValidering extends Validering {

        @Override
        protected BrugerManager newBrugerManager() {
            return new ValideringTest.MockBrugerManager();
        }
    }
}
