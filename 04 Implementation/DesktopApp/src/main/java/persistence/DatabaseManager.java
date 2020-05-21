package persistence;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import domain.Besked;
import domain.Bruger;
import domain.Chat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Benjamin
 */
public class DatabaseManager {
    private static DatabaseManager databaseManager; /** static, så vi altid kan få fat i den sammme DatabaseManager */
    private Firestore firestore;

    private DatabaseManager() throws IOException {
        initializeDB();

        /** initializeDB() skal kaldes før firestore kan initaliseres */
        firestore = FirestoreClient.getFirestore();
    }

    /** Der må kun være én instans af DatabaseManager, så derfor bruger vi altid getInstance(), når vi skal have fat i
     * DatabaseManager */
    public static synchronized DatabaseManager getInstance() throws IOException {
        if (databaseManager == null){
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public void initializeDB() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("./serviceAccount.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fysiodb-680ee.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        firestore = FirestoreClient.getFirestore();
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
        Bruger bruger = null;
        ApiFuture<DocumentSnapshot> document = firestore.collection("brugere").document(email).get();

        try {
            if (document.get().exists()) {
                bruger = document.get().toObject(Bruger.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return bruger;
    }

    public Chat hentChat(String afsender, String modtager, String emne) {
        Chat chat = null;
        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager).whereEqualTo("emne", emne);
        ApiFuture<QuerySnapshot> document = query.get();

        try {
            if (!document.get().isEmpty()){
                QueryDocumentSnapshot qds = document.get().getDocuments().get(0);
                chat = qds.toObject(Chat.class);
                DocumentReference reference = qds.getReference();
                chat.setBeskeder(hentBeskeder(reference));
            }
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return chat;
    }

    public ArrayList<Besked> hentBeskeder(DocumentReference reference){
        ArrayList<Besked> beskeder = new ArrayList<>();
        ApiFuture<QuerySnapshot> document = reference.collection("beskeder").get();

        try{
            if (!document.get().isEmpty()){
                List<QueryDocumentSnapshot> list = document.get().getDocuments();
                for (int i = 0; i < list.size(); i++) {
                    Besked besked = list.get(i).toObject(Besked.class);
                    beskeder.add(besked);
                }
            }
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return beskeder;
    }

    public void opdaterChat(Chat chat, Besked besked){
        String afsender = chat.getAfsender();
        String modtager = chat.getModtager();
        String emne = chat.getEmne();

        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager).whereEqualTo("emne", emne);

        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()){
                QueryDocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                DocumentReference reference = documentSnapshot.getReference();
                reference.collection("beskeder").document().set(besked);
            }
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}
