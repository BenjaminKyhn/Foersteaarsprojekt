package domain;

/** @author Benjamin */
public class Bruger {
    private String navn;
    private String email;
    private String password;
    private String fotoURL;

    public Bruger(){
    }

    public Bruger(String navn, String email, String password){
        this.navn = navn;
        this.email = email;
        this.password = password;
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
}