package domain;

/** @author Benjamin */
public class Besked {
    private String besked;
    private String tidspunkt;

    public Besked(){
    }

    public Besked(String besked){
        this.besked = besked;
    }

    public String getBesked() {
        return besked;
    }

    public void setBesked(String besked) {
        this.besked = besked;
    }

    public String getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(String tidspunkt) {
        this.tidspunkt = tidspunkt;
    }

    //TODO: Beskeder skal holde på afsender og modtager
}
