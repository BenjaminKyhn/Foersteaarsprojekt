package entities;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/** @author Benjamin */
public class Begivenhed implements Callable<Begivenhed> {
    String titel;
    String kategori;
    String id;
    long startTidspunkt;
    long slutTidspunkt;
    ArrayList<String> deltagere;

    public Begivenhed(){
    }

    public Begivenhed(String titel, String kategori, long startTidspunkt, long slutTidspunkt){
        this.titel = titel;
        this.kategori = kategori;
        this.startTidspunkt = startTidspunkt;
        this.slutTidspunkt = slutTidspunkt;
    }

    public Begivenhed(String titel, String kategori, long startTidspunkt, long slutTidspunkt, String id, ArrayList<String> deltagere){
        this.titel = titel;
        this.kategori = kategori;
        this.id = id;
        this.startTidspunkt = startTidspunkt;
        this.slutTidspunkt = slutTidspunkt;
        this.deltagere = deltagere;
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

    public ArrayList<String> getDeltagere() {
        return deltagere;
    }

    public void setDeltagere(ArrayList<String> deltagere) {
        this.deltagere = deltagere;
    }

    @Override
    public Begivenhed call() throws Exception {
        return new Begivenhed();
    }
}