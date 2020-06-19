package model;

import database.DatabaseManager;
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
    private DatabaseManager databaseManager;

    BookingManager() {
        databaseManager = DatabaseManager.getInstance();
        support = new PropertyChangeSupport(this);
    }

    public static synchronized BookingManager getInstance() {
        if (bookingManager == null) {
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

    public void gemBegivenhed(Begivenhed begivenhed) {
        begivenheder.add(begivenhed);
    }

    public void gemBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
//        support.firePropertyChange("gemBegivenheder", null, begivenheder);
    }

    public void fjernBegivenhed(int index) {
        begivenheder.remove(index);
    }

    public void tilfoejObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
