package model;

import entities.Begivenhed;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class BookingManager {
    private static BookingManager bookingManager;
    private PropertyChangeSupport support;
    private ArrayList<Begivenhed> begivenheder;

    BookingManager(){
        support = new PropertyChangeSupport(this);
    }

    public static synchronized BookingManager getInstance() {
        if (bookingManager == null){
            bookingManager = new BookingManager();
        }
        return bookingManager;
    }

    public ArrayList<Begivenhed> hentBegivenheder() {
        return begivenheder;
    }

    public void angivBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
    }

    public void gemBegivenhed(Begivenhed begivenhed){
        begivenheder.add(begivenhed);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
}
