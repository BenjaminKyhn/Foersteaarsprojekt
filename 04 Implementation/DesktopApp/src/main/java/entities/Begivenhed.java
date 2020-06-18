package entities;

import com.google.cloud.Timestamp;

/** @author Benjamin */
public class Begivenhed {
    String titel;
    String kategori;
    long startTidspunkt;
    long slutTidspunkt;
    String id;

    public Begivenhed(){
    }

    public Begivenhed(String titel, String kategori, long startTidspunkt, long slutTidspunkt){
        this.titel = titel;
        this.kategori = kategori;
        this.startTidspunkt = startTidspunkt;
        this.slutTidspunkt = slutTidspunkt;
    }

    public Begivenhed(String titel, String kategori, long startTidspunkt, long slutTidspunkt, String id){
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

    public long getStartTidspunkt() {
        return startTidspunkt;
    }

    public void setStartTidspunkt(long startTidspunkt) {
        this.startTidspunkt = startTidspunkt;
    }

    public long getSlutTidspunkt() {
        return slutTidspunkt;
    }

    public void setSlutTidspunkt(long slutTidspunkt) {
        this.slutTidspunkt = slutTidspunkt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
