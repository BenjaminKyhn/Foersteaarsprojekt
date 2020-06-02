package usecases;

import entities.Bruger;
import entities.exceptions.EksisterendeBrugerException;
import entities.exceptions.TomEmailException;
import entities.exceptions.TomNavnException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**@author Kelvin**/
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



        private class MockBrugerManager extends BrugerManager {
            @Override
            public Bruger hentBrugerMedEmail(String email) {
                if (email.equals("fys@frbsport.dk")) {
                    return new MockBruger("fys@frbsport.dk");
                }
                else {
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



    private class TestbarValidering extends Validering{

        @Override
        protected BrugerManager newBrugerManager() {
            return new ValideringTest.MockBrugerManager();
        }
    }
}
