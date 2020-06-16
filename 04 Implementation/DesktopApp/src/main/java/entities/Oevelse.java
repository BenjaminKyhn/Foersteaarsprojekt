package entities;

/**
 * @author Benjamin
 */
public class Oevelse {
    String navn;
    String kategori;
    String videoURL;

    public Oevelse(String kategori, String navn){
        this.kategori = kategori;
        this.navn = navn;
    }

    public Oevelse(){
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
