package domain;

/** @author Benjamin */
public class Bruger {
    private String navn;
    private String email;
    private String password;

    public Bruger(){
    }

    public Bruger(String navn, String email, String password){
        this.navn = navn;
        this.email = email;
        this.password = password;
    }
}
