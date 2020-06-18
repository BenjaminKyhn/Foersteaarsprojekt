package entities;

import com.google.cloud.Timestamp;

/** @author Benjamin */
public class Begivenhed {
    String titel;
    String kategori;
    Timestamp startTidspunkt;
    Timestamp slutTidspunkt;
    String id;

    public Begivenhed(){
    }

    public Begivenhed(String titel, String kategori, Timestamp startTidspunkt, Timestamp slutTidspunkt){
        this.titel = titel;
        this.kategori = kategori;
        this.startTidspunkt = startTidspunkt;
        this.slutTidspunkt = slutTidspunkt;
    }

    public Begivenhed(String titel, String kategori, Timestamp startTidspunkt, Timestamp slutTidspunkt, String id){
        this.titel = titel;
        this.kategori = kategori;
        this.startTidspunkt = startTidspunkt;
        this.slutTidspunkt = slutTidspunkt;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
