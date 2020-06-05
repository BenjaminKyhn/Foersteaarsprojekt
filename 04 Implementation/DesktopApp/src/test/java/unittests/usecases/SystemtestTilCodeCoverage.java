package unittests.usecases;

import entities.Bruger;
import entities.Chat;
import entities.exceptions.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SystemtestTilCodeCoverage {
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
        // aktivBruger er ikke null her?
        brugerFacade.opretBruger("Søren Nielsen", "soerenn@gmail.com", "testpw", false);
        List<Bruger> midlertidigListe = brugerFacade.hentBrugere();
        String output = brugerFacade.hentBrugere().get(midlertidigListe.size() - 1).getNavn();
        assertEquals("Søren Nielsen", output);
    }

    @Test
    public void sletBrugerST020101() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger bruger = brugerFacade.hentBrugere().get(0);
        brugerFacade.sletBruger(bruger, "testpw");
    }

    @Test
    public void sletBrugerST020102() {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger bruger = brugerFacade.hentBrugere().get(0);
        assertThrows(ForkertPasswordException.class, () -> brugerFacade.sletBruger(bruger, "forkertpw"));
    }

    @Test
    public void opretChatST030301() throws BrugerFindesIkkeException, ForkertPasswordException, TomEmneException, ForMangeTegnException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        beskedFacade.setChats(mockDatabaseManager.hentChats());


        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        brugerFacade.logInd("fys@frbsport.dk", "testpw");

        beskedFacade.opretChat("Karsten Wiren", "Ondt i ryggen");
        assertEquals("Ondt i ryggen", beskedFacade.hentChats().get(beskedFacade.hentChats().size() - 1).getEmne());

        brugerFacade.logUd();
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

        brugerFacade.logUd();
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

        brugerFacade.logUd();
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

        brugerFacade.logUd();
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

        brugerFacade.logUd();
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

        brugerFacade.logUd();
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

        brugerFacade.logUd();
    }

    private class MockDatabaseManager {
        public ObserverbarListe<Chat> hentChats() {
            ObserverbarListe<Chat> chats = new ObserverbarListe<>();
            Chat chat = new Chat("Karsten Wiren", "Christian Iuul", "Hold i nakken", System.currentTimeMillis());
            chats.add(chat);
            return chats;
        }

        public ObserverbarListe<Bruger> hentBrugere() {

            TekstHasher tekstHasher = new TekstHasher();
            String password = tekstHasher.hashTekst("testpw");

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
