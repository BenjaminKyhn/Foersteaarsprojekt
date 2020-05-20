package persistence;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
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

    public void initializeDB() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("./serviceAccount.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fysiodb-680ee.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        testDB();
    }

    public void testDB(){
        Map<String, Object> Brugermap = new HashMap<>();
        save(Brugermap, "Tommy", "tommyboi@gmail.com");
    }

    public static void read() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = dbFirestore.collection("users").document("tommyboi@gmail.com");
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        if (document.exists()) {
            String navn = document.get("navn").toString();
            System.out.println(navn);
        }
    }

    public static void save(Map<String, Object> entry, String navn, String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        entry.put("navn", navn);
        dbFirestore.collection("brugere").document(id).set(entry);
    }

    public Bruger hentBrugerMedEmail(String email){
        Bruger bruger = new Bruger();
        return bruger;
    }
}
