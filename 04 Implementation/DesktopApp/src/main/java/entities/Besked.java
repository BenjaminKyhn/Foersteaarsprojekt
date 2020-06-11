package entities;

/**
 * @author Benjamin
 * Beskedobjektet bruges til at holde på informationer om alle de beskeder, der bliver sendt mellem brugere
 */
public class Besked {
    private String besked;
    private long tidspunkt;
    private String afsender;
    private String modtager;

    /**
     * Den tomme constructor bruges i test
     */
    public Besked(){
    }

    /**
     * Denne constructor bruges til at instantiere nye beskeder.
     * @param afsender  navn på afsender
     * @param modtager  navn på modtager
     * @param besked    indholdet af beskeden
     * @param tidspunkt tidspunktet, hvor beskeden er afsendt
     */
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