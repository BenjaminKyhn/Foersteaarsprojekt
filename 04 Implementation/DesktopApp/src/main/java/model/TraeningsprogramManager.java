package model;

import entities.Bruger;
import entities.Oevelse;
import entities.Traeningsprogram;
import entities.exceptions.BrugerFindesIkkeException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramManager {
    ArrayList<Oevelse> oevelser;
    ArrayList<Traeningsprogram> programmer;
    PropertyChangeSupport support;
    BrugerManager brugerManager;

    public TraeningsprogramManager() {
        brugerManager = BrugerManager.getInstance();
        support = new PropertyChangeSupport(this);
    }

    public void tjekProgram(String email) throws BrugerFindesIkkeException {
        boolean brugerEksisterer = false;
        ArrayList<Bruger> brugere = brugerManager.hentBrugere();
        for (int i = 0; i < brugere.size(); i++) {
            if (brugere.get(i).getEmail().equals(email)){
                brugerEksisterer= true;
                break;
            }
        }

        if (!brugerEksisterer)
            throw new BrugerFindesIkkeException();

        for (int i = 0; i < programmer.size(); i++) {
            if (programmer.get(i).getPatientEmail().equals(email)){
                programmer.remove(i);
                break;
            }
        }
    }

    public void tildelProgram(String email, ArrayList<String> patientOevelser) throws BrugerFindesIkkeException {
        tjekProgram(email);
        Traeningsprogram program = new Traeningsprogram(email, patientOevelser);
        programmer.add(program);
        support.firePropertyChange("gemProgram", null, program);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    public ArrayList<Oevelse> hentOevelser() {
        return oevelser;
    }

    public void angivOevelser(ArrayList<Oevelse> oevelser) {
        this.oevelser = oevelser;
    }

    public ArrayList<Traeningsprogram> hentProgrammer() {
        return programmer;
    }

    public void angivProgrammer(ArrayList<Traeningsprogram> programmer) {
        this.programmer = programmer;
    }
}
