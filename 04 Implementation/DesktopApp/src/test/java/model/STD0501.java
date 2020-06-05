package model;

import entities.Bruger;
import entities.exceptions.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class STD0501 {
    @Test
    public void tilknytBehandlerST050101() throws ForkertRolleException, BehandlerFindesAlleredeException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger patient = brugerFacade.hentBrugere().get(1);
        Bruger behandler = brugerFacade.hentBrugere().get(0);
        brugerFacade.tilknytBehandler(patient, behandler);
        assertEquals("Christian Iuul", patient.getBehandlere().get(0));
    }

    @Test
    public void tilknytBehandlerST050102() throws ForkertRolleException, BehandlerFindesAlleredeException {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger patient = brugerFacade.hentBrugere().get(1);
        Bruger behandler = brugerFacade.hentBrugere().get(0);
        brugerFacade.tilknytBehandler(patient, behandler);
        assertThrows(BehandlerFindesAlleredeException.class, () -> brugerFacade.tilknytBehandler(patient, behandler));
    }

    @Test
    public void tilknytBehandlerST050103() {
        MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
        Bruger patient = brugerFacade.hentBrugere().get(0);
        Bruger behandler = brugerFacade.hentBrugere().get(1);
        assertThrows(ForkertRolleException.class, () -> brugerFacade.tilknytBehandler(patient, behandler));
    }

    private class MockDatabaseManager {
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
