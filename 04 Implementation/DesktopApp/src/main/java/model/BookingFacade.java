package model;

import entities.Aftale;

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

    public ArrayList<Aftale> hentAftaler() {
        return bookingManager.hentAftaler();
    }

    public void angivAftaler(ArrayList<Aftale> aftaler) {
        bookingManager.angivAftaler(aftaler);
    }

    public void gemAftale(Aftale aftale){
        bookingManager.gemAftale(aftale);
    }
}
