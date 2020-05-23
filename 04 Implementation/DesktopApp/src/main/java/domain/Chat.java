package domain;

import java.util.ArrayList;

/** @author Benjamin */
public class Chat {
    private String afsender;
    private String modtager;
    private String emne;
    private ArrayList<Besked> beskeder;

    public Chat(){
    }

    public Chat(String afsender, String modtager, String emne){
        this.afsender = afsender;
        this.modtager = modtager;
        this.emne = emne;
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

    public void tilfoejBesked(Besked besked){
        beskeder.add(besked);
    }
}
