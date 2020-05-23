package domain;

import java.util.ArrayList;

/** @author Benjamin */
public class Chat {
    private String afsender;
    private String modtager;
    private String emne;
    private String sidstAktiv;
    private ArrayList<Besked> beskeder;

    public Chat(){
    }

    public Chat(String afsender, String modtager, String emne, String sidstAktiv){
        this.afsender = afsender;
        this.modtager = modtager;
        this.emne = emne;
        this.sidstAktiv = sidstAktiv;
    }

    public void tilfoejBesked(Besked besked){
        beskeder.add(besked);
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

    public String getEmne() {
        return emne;
    }

    public void setEmne(String emne) {
        this.emne = emne;
    }

    public ArrayList<Besked> getBeskeder() {
        return beskeder;
    }

    public void setBeskeder(ArrayList<Besked> beskeder) {
        this.beskeder = beskeder;
    }

    public String getSidstAktiv() {
        return sidstAktiv;
    }

    public void setSidstAktiv(String sidstAktiv) {
        this.sidstAktiv = sidstAktiv;
    }
}
