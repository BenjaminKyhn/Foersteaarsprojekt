package entities;

import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class Traeningsprogram {
    ArrayList<String> oevelser;
    String patientNavn;
    String patientEmail;

    public Traeningsprogram(){
    }

    public Traeningsprogram(String patientEmail, ArrayList<String> oevelser){
        this.oevelser = oevelser;
        this.patientEmail = patientEmail;
    }

    public ArrayList<String> getOevelser() {
        return oevelser;
    }

    public void setOevelser(ArrayList<String> oevelser) {
        this.oevelser = oevelser;
    }

    public String getPatientNavn() {
        return patientNavn;
    }

    public void setPatientNavn(String patientNavn) {
        this.patientNavn = patientNavn;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}
