package model;

import entities.Bruger;
import entities.Chat;
import entities.exceptions.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** @author Benjamin */
public class STD03 {
    /** Vi instantierer en MockDatabaseManager for at fylde listen af chats og vi instantierer en BrugerFacade for at
     *  kunne kalde logInd*/
    @Test
    public void opretChatST030301() throws BrugerFindesIkkeException, ForkertPasswordException,
            TomEmneException, ForMangeTegnException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        beskedFacade.opretChat("Karsten Wiren", "Ondt i ryggen");
        String output = beskedFacade.hentChats().get(beskedFacade.hentChats().size() - 1).getEmne();
        assertEquals("Ondt i ryggen", output);
    }

    @Test
    public void opretChatST030302() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        assertThrows(BrugerFindesIkkeException.class, () -> beskedFacade.opretChat("Ejnar Gunnarsen", "Dårligt knæ"));
    }

    @Test
    public void opretChatST030303() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        assertThrows(TomEmneException.class, () -> beskedFacade.opretChat("Karsten Wiren", ""));
    }

    @Test
    public void opretChatST030304() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        assertThrows(ForMangeTegnException.class, () -> beskedFacade.opretChat("Karsten Wiren", "testtesttesttesttesttesttesttesttesttesttesttesttest"));
    }

    @Test
    public void sendBeskedST030501() throws ForkertPasswordException, TomBeskedException, ForMangeTegnException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("karstenw@gmail.com", "testpw");

        Chat chat = beskedFacade.hentChats().get(0);
        beskedFacade.sendBesked("Hej Christian", chat);
        assertEquals("Hej Christian", beskedFacade.hentBeskeder(chat).get(0).getBesked());
    }

    @Test
    public void sendBeskedST030502() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("karstenw@gmail.com", "testpw");

        Chat chat = beskedFacade.hentChats().get(0);
        assertThrows(TomBeskedException.class, () -> beskedFacade.sendBesked("", chat));
    }

    @Test
    public void sendBeskedST030503() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());

        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("karstenw@gmail.com", "testpw");

        Chat chat = beskedFacade.hentChats().get(0);
        assertThrows(ForMangeTegnException.class, () -> beskedFacade.sendBesked("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttestt", chat));
    }

    private class MockDatabaseManager {
        public ArrayList<Chat> hentChats() {
            ArrayList<Chat> chats = new ArrayList<>();
            Chat chat = new Chat("Karsten Wiren", "Christian Iuul",
                    "Hold i nakken", System.currentTimeMillis());
            chats.add(chat);
            return chats;
        }

        public ArrayList<Bruger> hentBrugere() {

            TekstHasher tekstHasher = new TekstHasher();
            String password = tekstHasher.hashTekst("testpw");

            ArrayList<Bruger> brugere = new ArrayList<>();
            Bruger behandler1 = new Bruger("Christian Iuul", "fys@frbsport.dk",
                    password, true);
            Bruger patient1 = new Bruger("Camilla Kron", "camillak@gmail.com",
                    password, false);
            Bruger patient2 = new Bruger("Karsten Wiren", "karstenw@gmail.com",
                    password, false);
            brugere.add(behandler1);
            brugere.add(patient1);
            brugere.add(patient2);
            return brugere;
        }
    }
}
