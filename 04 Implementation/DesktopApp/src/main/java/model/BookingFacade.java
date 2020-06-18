package model;

import entities.Begivenhed;

import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class BookingFacade {
    private BookingManager bookingManager;
    private static BookingFacade bookingFacade;

    private BookingFacade() {
        bookingManager = BookingManager.getInstance();
    }

    public static synchronized BookingFacade getInstance() {
        if (bookingFacade == null){
            bookingFacade = new BookingFacade();
        }
        return bookingFacade;
    }

    public ArrayList<Begivenhed> hentBegivenheder() {
        return bookingManager.hentBegivenheder();
    }

    public void angivBegivenheder(ArrayList<Begivenhed> aftaler) {
        bookingManager.angivBegivenheder(aftaler);
    }

    public void gemBegivenhed(Begivenhed begivenhed){
        bookingManager.gemBegivenhed(begivenhed);
    }
}
