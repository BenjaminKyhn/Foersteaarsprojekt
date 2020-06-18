package model;

import entities.Aftale;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class BookingManager {
    private static BookingManager bookingManager;
    private PropertyChangeSupport support;
    private ArrayList<Aftale> aftaler;

    BookingManager(){
        support = new PropertyChangeSupport(this);
    }

    public static synchronized BookingManager getInstance() {
        if (bookingManager == null){
            bookingManager = new BookingManager();
        }
        return bookingManager;
    }

    public ArrayList<Aftale> hentAftaler() {
        return aftaler;
    }

    public void angivAftaler(ArrayList<Aftale> aftaler) {
        this.aftaler = aftaler;
    }

    public void gemAftale(Aftale aftale){
        aftaler.add(aftale);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
}
