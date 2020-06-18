package model;

import entities.Aftale;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

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
}
