package model;

import domain.Bruger;
import domain.Chat;
import model.exceptions.BrugerFindesIkkeException;

import java.sql.Timestamp;

public class BeskedManagerTest {
    private MockDatabaseManager mockDatabaseManager;
    private BrugerManager mockBrugerManager;

    public void opretChat(String navn, String emne) throws BrugerFindesIkkeException {
        mockDatabaseManager = new MockDatabaseManager();
        Bruger afsender = mockBrugerManager.getAktivBruger();
        Bruger modtager = mockDatabaseManager.hentBrugerMedNavn(navn);
        String sidstAktiv = new Timestamp(System.currentTimeMillis()).toString();
        if (modtager == null)
            throw new BrugerFindesIkkeException();
        Chat nyChat = new Chat(afsender.getNavn(), modtager.getNavn(), emne, sidstAktiv);
        mockDatabaseManager.opretChat(nyChat);
    }

    private class MockDatabaseManager {
        public MockDatabaseManager(){

        }

        public void opretChat(Chat chat){
            System.out.println("Chat er oprettet");
        }

        public Bruger hentBrugerMedNavn(String navn){
            return new Bruger();
        }
    }

    private class MockBrugerManager {
        public Bruger getAktivBruger(){
            return new Bruger();
        }
    }
}
