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
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Benjamin
 */
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

    public void testDB() {
        Bruger bruger = new Bruger("Benny", "bennyboi@gmail.com", "rshguuruj");
        sletBruger(bruger);
    }

    public void gemBruger(Bruger bruger) {
        String email = bruger.getEmail();
        firestore.collection("brugere").document(email).create(bruger);
    }

    public void sletBruger(Bruger bruger){
        String email = bruger.getEmail();
        firestore.collection("brugere").document(email).delete();
    }

    public Bruger hentBrugerMedEmail(String email) {
        ApiFuture<DocumentSnapshot> document = firestore.collection("brugere").document(email).get();
        Bruger bruger = null;

        try {
            if (document.get().exists()) {
                bruger = document.get().toObject(Bruger.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return bruger;
    }
}
