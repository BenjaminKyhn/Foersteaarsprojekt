package database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import entities.Besked;
import entities.Bruger;
import entities.Chat;

import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**@author Benjamin*/
public class DatabaseManager {
    private static DatabaseManager databaseManager;
    /**
     * static, så vi altid kan få fat i den sammme DatabaseManager
     */
    private Firestore firestore;
    private boolean write;

    private DatabaseManager() {
        initializeDB();

        /** initializeDB() skal kaldes før firestore kan initaliseres */
        firestore = FirestoreClient.getFirestore();
    }

    /**
     * Der må kun være én instans af DatabaseManager, så derfor bruger vi altid getInstance(), når vi skal have fat i
     * DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public void initializeDB() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("./serviceAccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://fysiodb-680ee.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

            firestore = FirestoreClient.getFirestore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gemBruger(Bruger bruger) {
        String email = bruger.getEmail();
        firestore.collection("brugere").document(email).create(bruger);
    }

    public void sletBruger(Bruger bruger) {
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

    public Bruger hentBrugerMedNavn(String navn) {
        Bruger bruger = null;
        Query query = firestore.collection("brugere").whereEqualTo("navn", navn);

        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot qds = querySnapshot.getDocuments().get(0);
                bruger = qds.toObject(Bruger.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return bruger;
    }

    public ArrayList<Bruger> hentBrugere() {
        ArrayList<Bruger> brugere = new ArrayList<>();
        Query query = firestore.collection("brugere");

        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                for (int i = 0; i < querySnapshot.size(); i++) {
                    brugere.add(querySnapshot.getDocuments().get(i).toObject(Bruger.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return brugere;
    }

    public void opretChat(Chat chat) {
        firestore.collection("chats").document().create(chat);
    }

    public ArrayList<Chat> hentChatsMedNavn(String navn) {
        ArrayList<Chat> chats = new ArrayList<>();

        /** Lav 2 queries, fordi navnet både kan være afsender og modtager */
        Query query1 = firestore.collection("chats").whereEqualTo("afsender", navn);
        Query query2 = firestore.collection("chats").whereEqualTo("modtager", navn);

        /** Tilføj alle chats, hvor navnet er afsender til listen af chats */
        try {
            QuerySnapshot querySnapshot1 = query1.get().get();
            if (!querySnapshot1.isEmpty()) {
                for (int i = 0; i < querySnapshot1.size(); i++) {
                    QueryDocumentSnapshot qds = querySnapshot1.getDocuments().get(i);
                    Chat chat = qds.toObject(Chat.class);
                    DocumentReference reference = qds.getReference();
                    chat.setBeskeder(hentBeskeder(reference));
                    chats.add(chat);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /** Tilføj alle chats, hvor navnet er modtager til listen af chats */
        try {
            QuerySnapshot querySnapshot2 = query2.get().get();
            if (!querySnapshot2.isEmpty()) {
                for (int i = 0; i < querySnapshot2.size(); i++) {
                    QueryDocumentSnapshot qds = querySnapshot2.getDocuments().get(i);
                    Chat chat = qds.toObject(Chat.class);
                    DocumentReference reference = qds.getReference();
                    chat.setBeskeder(hentBeskeder(reference));
                    chats.add(chat);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /** Sorter listen */
        Collections.sort(chats, Chat.ChatTidspunktComparator);

        /** Returner listen */
        return chats;
    }

    public Chat hentChat(String afsender, String modtager, String emne) {
        Chat chat = null;
        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager).whereEqualTo("emne", emne);

        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot qds = querySnapshot.getDocuments().get(0);
                chat = qds.toObject(Chat.class);
                DocumentReference reference = qds.getReference();
                chat.setBeskeder(hentBeskeder(reference));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return chat;
    }

    public void opdaterChat(Chat chat) {
        String afsender = chat.getAfsender();
        String modtager = chat.getModtager();
        String emne = chat.getEmne();

        write = true;

        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager).whereEqualTo("emne", emne);

        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot documentSnapshot = querySnapshot.getDocumentChanges().get(0).getDocument(); /** vi bruger getDocumentChanges for at minimere antallet af reads til databasen. Så bliver Snapshottet kun hentet, hvis der er en ændring */
                DocumentReference reference = documentSnapshot.getReference();

                int sidsteIndex = chat.getBeskeder().size() - 1;
                Besked besked = chat.getBeskeder().get(sidsteIndex);
                reference.collection("beskeder").document().set(besked);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Besked> hentBeskeder(DocumentReference reference) {
        ArrayList<Besked> beskeder = new ArrayList<>();
        ApiFuture<QuerySnapshot> document = reference.collection("beskeder").orderBy("tidspunkt").get();

        try {
            if (!document.get().isEmpty()) {
                List<QueryDocumentSnapshot> list = document.get().getDocuments();
                for (int i = 0; i < list.size(); i++) {
                    Besked besked = list.get(i).toObject(Besked.class);
                    beskeder.add(besked);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return beskeder;
    }

    public void observerKaldFraFirestoreTilChat(Chat chat) {
        String afsender = chat.getAfsender();
        String modtager = chat.getModtager();
        String emne = chat.getEmne();
        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager)
                .whereEqualTo("emne", emne);

        try {
            query.get().get().getDocuments().get(0).getReference().collection("beskeder").orderBy("tidspunkt").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                    if (!write) {
                        if (value != null) {
                            List<QueryDocumentSnapshot> list = value.getDocuments();
                            if (list.size() != chat.getBeskeder().size()) {

                                Besked besked = list.get(list.size() - 1).toObject(Besked.class);
                                chat.tilfoejBesked(besked);
                            }
                        }
                    } else {
                        write = false;
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void opdaterTraeningsprogram(Bruger bruger, ArrayList<String> program) {
        ArrayList<Object> cast = new ArrayList<>(program);
        Map<String, Object> map = new HashMap<>();
        map.put("program", cast);
        firestore.collection("træningsprogram")
                .document(bruger.getEmail())
                .set(map);

    }

    public void opdaterBruger(Bruger bruger){
        firestore.collection("brugere").document(bruger.getEmail()).set(bruger);
    }
}

//TODO får beskeder til at opdateres live med addSnapshotListener