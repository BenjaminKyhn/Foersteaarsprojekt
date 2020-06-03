package systemtest;

import entities.Bruger;
import entities.Chat;
import entities.exceptions.BrugerFindesIkkeException;
import entities.exceptions.ForkertPasswordException;
import org.junit.Test;
import unittests.usecases.*;

import static org.junit.Assert.*;

public class STD0301 {
    @Test
    public void opretChatST030101() throws BrugerFindesIkkeException, ForkertPasswordException {
        /** Vi instantierer en MockDatabaseManager for at fylde listen af chats */
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedManager beskedManager = BeskedManager.getInstance();
        beskedManager.setChats(mockDatabaseManager.hentChats());

        /** Vi instantierer en BrugerFacade for at kunne kalde logInd*/
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        /** Opret en chat */
        beskedManager.opretChat("Karsten Wiren", "Ondt i ryggen");
        assertEquals("Ondt i ryggen", beskedManager.hentChats().get(beskedManager.hentChats().size() - 1).getEmne());
    }

    private class MockDatabaseManager {
        public ObserverbarListe<Chat> hentChats() {
            ObserverbarListe<Chat> chats = new ObserverbarListe<>();
            return chats;
        }

        public ObserverbarListe<Bruger> hentBrugere() {
            /** Hash passwordet */
            TekstHasher tekstHasher = new TekstHasher();
            String password = tekstHasher.hashTekst("testpw");

            /** Lav en liste af brugere og returner den */
            ObserverbarListe<Bruger> brugere = new ObserverbarListe<>();
            Bruger behandler1 = new Bruger("Christian Iuul", "fys@frbsport.dk", password, true);
            Bruger patient1 = new Bruger("Camilla Kron", "camillak@gmail.com", password, false);
            Bruger patient2 = new Bruger("Karsten Wiren", "karstenw@gmail.com", password, false);
            brugere.add(behandler1);
            brugere.add(patient1);
            brugere.add(patient2);
            return brugere;
        }
    }
}
