package com.example.android.androidapp.usecases;

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
        for (String programoevelse : program) {
            if (programoevelse.equals(oevelse)) {
                program.remove(programoevelse);
            }
        }
    }

    public void angivListe(ObserverbarListe<String> liste) {
        program = liste;
    }

    public ObserverbarListe<String> hentListe() {
        return program;
    }

}
