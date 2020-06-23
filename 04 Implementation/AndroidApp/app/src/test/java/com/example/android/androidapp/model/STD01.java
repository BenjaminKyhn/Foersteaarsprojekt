package com.example.android.androidapp.model;

import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.entities.exceptions.TomEmailException;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;

    /**
     * @author Benjamin
     */
    public class STD01 {

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
        public void tjekEmailST010104() throws EksisterendeBrugerException {
            MockDatabaseManager mockDatabaseManager = new MockDatabaseManager();
            BrugerFacade brugerFacade = BrugerFacade.getInstance();
            brugerFacade.setBrugere(mockDatabaseManager.hentBrugere());
            String email = "fys@frbsport.dk";
            
        }

        @Test
        public void opretBrugerST010402(){
            // Assert på listen i MockDatabaseManager for at teste observerkaldet
        }

        /**
         * Vi kan ikke bruge den rigtige DatabaseManager i vores tests, fordi det vil være meget upraktisk at kalde metoder
         * som sletBruger og opretBruger i vores tests, da disse metoder vil forstyrre den egentlige funktionalitet af
         * programmet. Derfor opretter vi en ny klasse MockDatabaseManager, hvis eneste funktion er at fylde vores lister
         * af brugere, chats og beskeder med dummy data, som kan bruges til at udføre systemtests. Ud over DatabaseManager,
         * så bruger testene de egentlige klasser i programmet.
         */
        private class MockDatabaseManager {
            ArrayList<Bruger> brugere;

            /** Metoden hentBrugere efterligner metoden i den rigtige DatabaseManager
             * @return*/
            public ArrayList<Bruger> hentBrugere() {
                brugere = new ArrayList<>();
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
