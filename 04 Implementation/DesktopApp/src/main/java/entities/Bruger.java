package entities;

import java.util.ArrayList;

/**
 * @author Benjamin
 * Brugerobjektet bruges til at holde styr på patienterne og behandlers informationer. Passwordet er hashed, så det er
 * kun personen selv, der kender det.
 */
public class Bruger {
    private String navn;
    private String email;
    private String password;
    private String fotoURL;
    private boolean erBehandler;
    private ArrayList<String> behandlere;

    /**
     * Den tomme constructor bruges i test
     */
    public Bruger(){
    }

    /**
     * Denne constructor bruges, når der instantieres nye brugerobjekter
     * @param navn  brugerens fulde navn
     * @param email brugerens emailadresse
     * @param password  brugerens password (dette er hashed)
     * @param erBehandler   true, hvis brugeren er behandler hos klinikken og false, hvis brugeren er patient
     */
    public Bruger(String navn, String email, String password, boolean erBehandler){
        this.navn = navn;
        this.email = email;
        this.password = password;
        this.erBehandler = erBehandler;
        behandlere = new ArrayList<>();
    }

    public ArrayList<String> getBehandlere() {
        return behandlere;
    }

    public void setBehandlere(ArrayList<String> behandlere) {
        this.behandlere = behandlere;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public boolean isErBehandler() {
        return erBehandler;
    }

    public void setErBehandler(boolean erBehandler) {
        this.erBehandler = erBehandler;
    }
}