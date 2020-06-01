package entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;

/** @author Benjamin */
public class Chat {
    private String afsender;
    private String modtager;
    private String emne;
    private long sidstAktiv;
    private ArrayList<Besked> beskeder;
    private PropertyChangeSupport support;

    public Chat() {
        support = new PropertyChangeSupport(this);
    }

    public Chat(String afsender, String modtager, String emne, long sidstAktiv){
        this.afsender = afsender;
        this.modtager = modtager;
        this.emne = emne;
        this.sidstAktiv = sidstAktiv;
        beskeder = new ArrayList<>();
        support = new PropertyChangeSupport(this);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    public void tilfoejBesked(Besked besked){
        beskeder.add(besked);
        support.firePropertyChange("Ny Besked", null, this);
    }

    public String getAfsender() {
        return afsender;
    }

    public void setAfsender(String afsender) {
        this.afsender = afsender;
    }

    public String getModtager() {
        return modtager;
    }

    public void setModtager(String modtager) {
        this.modtager = modtager;
    }

    public String getEmne() {
        return emne;
    }

    public void setEmne(String emne) {
        this.emne = emne;
    }

    public ArrayList<Besked> getBeskeder() {
        return beskeder;
    }

    public void setBeskeder(ArrayList<Besked> beskeder) {
        this.beskeder = beskeder;
    }

    public long getSidstAktiv() {
        return sidstAktiv;
    }

    public void setSidstAktiv(long sidstAktiv) {
        this.sidstAktiv = sidstAktiv;
    }

    public static Comparator<Chat> ChatTidspunktComparator = new Comparator<Chat>() {
        @Override
        public int compare(Chat c1, Chat c2) {
            long tidspunkt1 = c1.getSidstAktiv();
            long tidspunkt2 = c2.getSidstAktiv();

            return Long.compare(tidspunkt2, tidspunkt1);
        }
    };
}