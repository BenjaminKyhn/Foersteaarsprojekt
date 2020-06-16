package entities;

import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class Traeningsprogram {
    ArrayList<Oevelse> oevelser;
    String patientNavn;

    public Traeningsprogram(){
    }

    public Traeningsprogram(String patientNavn, ArrayList<Oevelse> oevelser){
        this.patientNavn = patientNavn;
        this.oevelser = oevelser;
    }

    public ArrayList<Oevelse> getOevelser() {
        return oevelser;
    }

    public void setOevelser(ArrayList<Oevelse> oevelser) {
        this.oevelser = oevelser;
    }

    public String getPatientNavn() {
        return patientNavn;
    }

    public void setPatientNavn(String patientNavn) {
        this.patientNavn = patientNavn;
    }
}
