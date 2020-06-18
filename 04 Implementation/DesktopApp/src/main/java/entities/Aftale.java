package entities;

import com.google.cloud.Timestamp;

/** @author Benjamin */
public class Aftale {
    String titel;
    String kategori;
    Timestamp startTidspunkt;
    Timestamp slutTidspunkt;

    public Aftale(){
    }

    public Aftale(String titel, String kategori, Timestamp startTidspunkt, Timestamp slutTidspunkt){
        this.titel = titel;
        this.kategori = kategori;
        this.startTidspunkt = startTidspunkt;
        this.slutTidspunkt = slutTidspunkt;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Timestamp getStartTidspunkt() {
        return startTidspunkt;
    }

    public void setStartTidspunkt(Timestamp startTidspunkt) {
        this.startTidspunkt = startTidspunkt;
    }

    public Timestamp getSlutTidspunkt() {
        return slutTidspunkt;
    }

    public void setSlutTidspunkt(Timestamp slutTidspunkt) {
        this.slutTidspunkt = slutTidspunkt;
    }
}
