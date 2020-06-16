package model;

import entities.Oevelse;
import entities.Traeningsprogram;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/** @author Tommy */
public class TraeningsprogramManager {
    ArrayList<Oevelse> oevelser;
    ArrayList<Traeningsprogram> programmer;
    PropertyChangeSupport support;

    public TraeningsprogramManager() {
        support = new PropertyChangeSupport(this);
    }

    public void gemProgram(Traeningsprogram program){
        for (int i = 0; i < programmer.size(); i++) {
            if (programmer.get(i).getPatientEmail().equals(program.getPatientEmail()))
                programmer.remove(i);
        }
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
