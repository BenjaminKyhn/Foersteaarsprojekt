package systemtests;

import entities.Bruger;
import entities.exceptions.ForkertPasswordException;
import org.junit.Test;
import unittests.usecases.BrugerFacade;
import unittests.usecases.ObserverbarListe;
import unittests.usecases.TekstHasher;

import static org.junit.Assert.*;

public class STD0201 {

    @Test
    public void sletBrugerST020101() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger bruger = brugerFacade.hentBrugere().get(0);
        brugerFacade.sletBruger(bruger, "testpw");
    }

    @Test
    public void sletBrugerST020102() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger bruger = brugerFacade.hentBrugere().get(0);
        assertThrows(ForkertPasswordException.class, () -> brugerFacade.sletBruger(bruger, "forkertpw"));
    }

    private class MockDatabaseManager {
        public ObserverbarListe<Bruger> hentBrugere() {
            /** Vi starter med at hashe passwordet, fordi metoden sletBruger hasher det indtastede password. Dvs. det
             * password, der ligger i databasen, er nødt til at være hashed, før sletBruger kan lave et passwordtjek.*/
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
