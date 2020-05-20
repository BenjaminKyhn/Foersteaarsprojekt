package persistence;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import domain.Bruger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** @author Benjamin */
public class DatabaseManager {
    Firestore firestore;

    public DatabaseManager() throws IOException {
        initializeDB();

        /** initializeDB() skal kaldes før firestore kan initaliseres */
        firestore = FirestoreClient.getFirestore();
    }

    public void initializeDB() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("./serviceAccount.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fysiodb-680ee.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    public void testDB(){
        // Test hentBrugerMedEmail-metode
        Bruger bruger = hentBrugerMedEmail("kellyboi@gmail.com");
        System.out.println(bruger.getNavn());

        //Test save-metode
        Map<String, Object> Brugermap = new HashMap<>();
        save(Brugermap, "Kelvin", "kellyboi@gmail.com");
    }

    public void save(Map<String, Object> entry, String navn, String id) {
//        firestore = FirestoreClient.getFirestore();
        entry.put("navn", navn);
        firestore.collection("brugere").document(id).set(entry);
    }

    public Bruger hentBrugerMedEmail(String email) {
//        firestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> document = firestore.collection("brugere").document(email).get();
        Bruger bruger = null;

        try{
            if (document.get().exists()){
                bruger = document.get().toObject(Bruger.class);
            }
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return bruger;
    }
}
