package model;

import entities.Bruger;
import entities.exceptions.ForkertPasswordException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** @author Benjamin */
public class STD02 {

    @Test
    public void sletBrugerST020101() throws ForkertPasswordException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger bruger = brugerFacade.hentBrugere().get(0);
        brugerFacade.sletBruger(bruger, "testpw");
        assertNull(brugerFacade.hentBrugerMedNavn(bruger.getNavn()));
    }

    @Test
    public void sletBrugerST020102() {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger bruger = brugerFacade.hentBrugere().get(0);
        assertThrows(ForkertPasswordException.class, () -> brugerFacade.sletBruger(bruger, "forkertpw"));
    }

    private class MockDatabaseManager {
        /** Vi starter med at hashe passwordet, fordi metoden sletBruger hasher det indtastede password. Dvs. det
         * password, der ligger i listen af brugere, er nødt til at være hashed, før sletBruger kan lave et
         * passwordtjek.*/
        public ArrayList<Bruger> hentBrugere() {
            TekstHasher tekstHasher = new TekstHasher();
            String password = tekstHasher.hashTekst("testpw");

            ArrayList<Bruger> brugere = new ArrayList<>();
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
