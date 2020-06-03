package systemtest;

import entities.Bruger;
import entities.exceptions.*;
import unittests.usecases.BrugerFacade;
import org.junit.Test;
import unittests.usecases.ObserverbarListe;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Benjamin
 */
public class STD0101 {

    @Test
    public void tjekEmailST010101() {
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        String email = null;
        assertThrows(NullPointerException.class, () -> brugerFacade.tjekEmail(email));
    }

    @Test
    public void tjekEmailST010102() throws TomEmailException, EksisterendeBrugerException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        String email = "test@mail.dk";
        brugerFacade.tjekEmail(email);
    }

    @Test
    public void tjekEmailST010103() {
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        String email = "";
        assertThrows(TomEmailException.class, () -> brugerFacade.tjekEmail(email));
    }

    @Test
    public void tjekEmailST010104() {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        String email = "fys@frbsport.dk";
        assertThrows(EksisterendeBrugerException.class, () -> brugerFacade.tjekEmail(email));
    }

    @Test
    public void opretBrugerST010401() throws BrugerErIkkeBehandlerException, EksisterendeBrugerException, TomEmailException, TomNavnException, TomPasswordException, PasswordLaengdeException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.opretBruger("Søren Nielsen", "soerenn@gmail.com", "testpw", false);
        List<Bruger> midlertidigListe = brugerFacade.hentBrugere();
        String output = brugerFacade.hentBrugere().get(midlertidigListe.size() - 1).getNavn();
        assertEquals("Søren Nielsen", output);
    }

    /**
     * Vi kan ikke bruge den rigtige DatabaseManager i vores tests, fordi det vil være meget upraktisk at kalde metoder
     * som sletBruger og opretBruger i vores tests, da disse metoder vil forstyrre den egentlige funktionalitet af
     * programmet. Derfor opretter vi en ny klasse MockDatabaseManager, hvis eneste funktion er at fylde vores lister
     * af brugere, chats og beskeder med dummy data, som kan bruges til at udføre systemtests. Ud over DatabaseManager,
     * så bruger testene de egentlige klasser i programmet.
     */
    private class MockDatabaseManager {

        /** Metoden hentBrugere efterligner metoden i den rigtige DatabaseManager */
        public ObserverbarListe<Bruger> hentBrugere() {
            ObserverbarListe<Bruger> brugere = new ObserverbarListe<>();
            Bruger behandler1 = new Bruger("Christian Iuul", "fys@frbsport.dk", "testpw", true);
            Bruger patient1 = new Bruger("Camilla Kron", "camillak@gmail.com", "testpw", false);
            Bruger patient2 = new Bruger("Karsten Wiren", "karstenw@gmail.com", "testpw", false);
            brugere.add(behandler1);
            brugere.add(patient1);
            brugere.add(patient2);
            return brugere;
        }
    }
}
