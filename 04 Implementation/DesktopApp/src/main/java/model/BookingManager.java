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
        support.firePropertyChange("gemBegivenhed", null, begivenhed);
    }

    public void gemBegivenheder(ArrayList<Begivenhed> begivenheder) {
        this.begivenheder = begivenheder;
        for (int i = 0; i < begivenheder.size(); i++) {
            Begivenhed begivenhed = begivenheder.get(i);
            support.firePropertyChange("gemBegivenhed", null, begivenhed);
        }
    }

    public void sletBegivenhed(String id) {
        for (int i = 0; i < begivenheder.size(); i++) {
            if (begivenheder.get(i).getId().equals(id)){
                Begivenhed begivenhed = begivenheder.get(i);
                begivenheder.remove(begivenhed);
                support.firePropertyChange("sletBegivenhed", null, begivenhed);
            }
        }
    }

    public void tilfoejObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
