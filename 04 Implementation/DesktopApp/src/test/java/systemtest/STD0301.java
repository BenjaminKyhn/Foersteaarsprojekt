package systemtest;

import entities.Bruger;
import entities.Chat;
import entities.exceptions.*;
import org.junit.Test;
import unittests.usecases.*;

import static org.junit.Assert.*;

/** @author Benjamin */
public class STD0301 {
    @Test
    public void opretChatST030301() throws BrugerFindesIkkeException, ForkertPasswordException, TomEmneException, ForMangeTegnException {
        /** Vi instantierer en MockDatabaseManager for at fylde listen af chats */
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        /** Vi instantierer en BrugerFacade for at kunne kalde logInd*/
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        /** Opret en chat */
        beskedFacade.opretChat("Karsten Wiren", "Ondt i ryggen");
        assertEquals("Ondt i ryggen", beskedFacade.hentChats().get(beskedFacade.hentChats().size() - 1).getEmne());
    }

    @Test
    public void opretChatST030302() throws ForkertPasswordException {
        /** Vi instantierer en MockDatabaseManager for at fylde listen af chats */
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        /** Vi instantierer en BrugerFacade for at kunne kalde logInd*/
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        /** Opret en chat */
        assertThrows(BrugerFindesIkkeException.class, () -> beskedFacade.opretChat("Ejnar Gunnarsen", "Dårligt knæ"));
    }

    @Test
    public void sendBeskedST030501() throws ForkertPasswordException, TomBeskedException, ForMangeTegnException {
        /** Vi instantierer en MockDatabaseManager for at fylde listen af chats */
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        /** Vi instantierer en BrugerFacade for at kunne kalde logInd*/
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("karstenw@gmail.com", "testpw");

        Chat chat = beskedFacade.hentChats().get(0);
        beskedFacade.sendBesked("Hej Christian", chat);
        assertEquals("Hej Christian", beskedFacade.hentBeskeder(chat).get(0).getBesked());
    }

    private class MockDatabaseManager {
        public ObserverbarListe<Chat> hentChats() {
            ObserverbarListe<Chat> chats = new ObserverbarListe<>();
            Chat chat = new Chat("Karsten Wiren", "Christian Iuul", "Hold i nakken", System.currentTimeMillis());
            chats.add(chat);
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
