package domain;

/** @author Benjamin */
public class Besked {
    private String besked;
    private String tidspunkt;
    private String afsender;
    private String modtager;

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

    //TODO: Beskeder skal holde på afsender og modtager
}
