package entities;

/** @author Benjamin */
public class Besked {
    private String besked;
    private long tidspunkt;
    private String afsender;
    private String modtager;

    public Besked(){
    }

    public Besked(String afsender, String modtager, String besked, long tidspunkt){
        this.afsender = afsender;
        this.modtager = modtager;
        this.besked = besked;
        this.tidspunkt = tidspunkt;
    }

    public String getBesked() {
        return besked;
    }

    public void setBesked(String besked) {
        this.besked = besked;
    }

    public long getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(long tidspunkt) {
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
}