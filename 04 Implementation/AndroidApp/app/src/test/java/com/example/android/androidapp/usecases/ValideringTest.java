package com.example.android.androidapp.usecases;

import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.exceptions.EksisterendeBrugerException;
import com.example.android.androidapp.entities.exceptions.TomEmailException;
import com.example.android.androidapp.entities.exceptions.TomNavnException;
import com.example.android.androidapp.entities.exceptions.TomPasswordException;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/** @author Benjamin */
public class ValideringTest {
    @Test
    public void tjekEmailUT010101() {
        final Validering validering = new Validering();
        final String email = null;
        assertThrows(NullPointerException.class, () -> validering.tjekEmail(email));
    }

    @Test
    public void tjekEmailUT010102() {
        final Validering validering = new TestbarValidering();
        final String email = "fys@frbsport.dk";
        assertThrows(EksisterendeBrugerException.class, () -> validering.tjekEmail(email));
    }

    @Test
    public void tjekEmailUT010103() {
        final Validering validering = new Validering();
        final String email = "";
        assertThrows(TomEmailException.class, () -> validering.tjekEmail(email));
    }

    @Test
    public void tjekEmailUT010104() throws EksisterendeBrugerException, TomEmailException {
        Validering validering = new TestbarValidering();
        String email = "gertrud57@hotmail.com";
        validering.tjekEmail(email);
    }

    @Test
    public void tjekNavnUT010201() {
        final Validering validering = new Validering();
        final String navn = null;
        assertThrows(NullPointerException.class, () -> validering.tjekNavn(navn));
    }

    @Test
    public void tjekNavnUT010202() {
        final Validering validering = new Validering();
        final String navn = "";
        assertThrows(TomNavnException.class, () -> validering.tjekNavn(navn));
    }

    @Test
    public void tjekNavnUT010203() throws TomNavnException {
        Validering validering = new Validering();
        String navn = "Thomas Gustafsson";
        validering.tjekNavn(navn);
    }

    @Test
    public void tjekPasswordUT010301() {
        final Validering validering = new Validering();
        final String password = null;
        assertThrows(NullPointerException.class, () -> validering.tjekPassword(password));
    }

    @Test
    public void tjekPasswordUT010302() {
        final Validering validering = new Validering();
        final String password = "";
        assertThrows(TomPasswordException.class, () -> validering.tjekPassword(password));
    }

    private static class MockBrugerManager extends BrugerManager {

        @Override
        Bruger hentBrugerMedEmail(String email) {
            if (email.equals("fys@frbsport.dk")) {
                return new MockBruger();
            }
            return null;
        }
    }

    private static class MockBruger extends Bruger {
        String email;

        public MockBruger() {
            email = "fys@frbsport.dk";
        }
    }

    private static class TestbarValidering extends Validering{
        @Override
        protected BrugerManager newBrugerManager() {
            return new MockBrugerManager();
        }
    }
}
