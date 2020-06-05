package model;

/** @author Tommy */
public class TraeningsprogramManager {
    ObserverbarListe<String> program;

    public TraeningsprogramManager() {
        program = new ObserverbarListe<>();
    }

    public void tilfoejOevelse(String oevelse) {
        program.add(oevelse);
    }

    public void fjernOevelse(String oevelse) {
        program.removeIf(programoevelse -> programoevelse.equals(oevelse));
    }

    public void angivListe(ObserverbarListe<String> liste) {
        program = liste;
    }

    public ObserverbarListe<String> hentListe() {
        return program;
    }

}
