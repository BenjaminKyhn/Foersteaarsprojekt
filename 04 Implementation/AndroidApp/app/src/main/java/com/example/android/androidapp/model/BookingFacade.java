package com.example.android.androidapp.model;



import com.example.android.androidapp.entities.Begivenhed;
import com.example.android.androidapp.entities.exceptions.OverlappendeBegivenhederException;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class BookingFacade {
    private BookingManager bookingManager;
    private static BookingFacade bookingFacade;

    private BookingFacade() {
        bookingManager = new BookingManager();
    }

    public static synchronized BookingFacade hentInstans() {
        if (bookingFacade == null){
            bookingFacade = new BookingFacade();
        }
        return bookingFacade;
    }

    public void rydObservere() {
        bookingManager.rydObservere();
    }

    public ArrayList<Begivenhed> hentBegivenheder() {
        return bookingManager.hentBegivenheder();
    }

    public ArrayList<Begivenhed> hentBegivenhederFraBehandlerMedDato(String behandler, long dato) {
        return bookingManager.hentBegivenhederFraBehandlerMedDato(behandler, dato);
    }

    public void angivBegivenheder(ArrayList<Begivenhed> aftaler) {
        bookingManager.angivBegivenheder(aftaler);
    }

    public void gemBegivenhed(Begivenhed begivenhed) throws OverlappendeBegivenhederException {
        bookingManager.gemBegivenhed(begivenhed);
    }

    public void sletBegivenhed(String id){
        bookingManager.sletBegivenhed(id);
    }

    public void tjekBegivenhed(Begivenhed begivenhed) throws OverlappendeBegivenhederException {
        bookingManager.tjekBegivenhed(begivenhed);
    }

    public void gemBegivenheder(ArrayList<Begivenhed> begivenheder) {
        bookingManager.gemBegivenheder(begivenheder);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        bookingManager.tilfoejObserver(listener);
    }
}
