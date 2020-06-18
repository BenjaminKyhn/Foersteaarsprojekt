package database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import entities.*;

import javax.annotation.Nullable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author Benjamin
 * DatabaseManager håndterer alle kald til Firebase Firestore.
 * Den kendes kun af UI'et for at holde en clean architecture.
 */
public class DatabaseManager {
    /** static, så vi altid kan få fat i den sammme DatabaseManager */
    private static DatabaseManager databaseManager;
    private Firestore firestore;
    private boolean write;
    private PropertyChangeSupport support;

    /**
     * Constructoren er private, fordi vi anvender singleton pattern.
     * initializeDB() skal kaldes før firestore kan initaliseres
     */
    private DatabaseManager() {
        initializeDB();
        support = new PropertyChangeSupport(this);
        firestore = FirestoreClient.getFirestore();
    }

    /**
     * Der må kun være én instans af DatabaseManager, så derfor bruger vi altid getInstance(), når vi skal have fat i
     * DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    /**
     * Skaber forbindelse til Firebase
     */
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

    /**
     * Gemmer brugeren i databasen i "brugere" collection. Documentet får samme navn som brugerobjektets email.
     * @param bruger et brugerobjekt skal først skabes og Firestore bruger brugerobjektets getters til at gemme
     *               instansvariable.
     */
    public void gemBruger(Bruger bruger) {
        String email = bruger.getEmail();
        firestore.collection("brugere").document(email).create(bruger);
    }

    /**
     * Sletter brugeren fra "brugere" collection i databasen
     * @param bruger igen bruges brugerobjektets getters til at afgøre, hvad der skal slettes i databasen
     */
    public void sletBruger(Bruger bruger) {
        String email = bruger.getEmail();
        firestore.collection("brugere").document(email).delete();
    }

    public void opdaterBruger(Bruger bruger){
        firestore.collection("brugere").document(bruger.getEmail()).set(bruger);
    }

    /**
     * Henter en bruger fra databasen, som har et field med en email, som svarer til parameteret email. Metoden
     * returnerer void, fordi vi bruger observer. Kaldet sker i en tråd og observeren fyrer en firePropertyChange, når
     * brugeren er hentet.
     * @param email kan være en hvilken som helst String
     */
    public void hentBrugerMedEmail(String email) {
        ApiFuture<DocumentSnapshot> document = firestore.collection("brugere").document(email).get();

        Thread thread = new Thread(() -> {
            try {
                Bruger bruger = null;
                if (document.get().exists()) {
                    bruger = document.get().toObject(Bruger.class);

                }
                support.firePropertyChange("hentBrugerMedEmail", null, bruger);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    /**
     * Henter alle brugere fra "brugere" collection og skaber brugerobjekter, som tilføjes til en ArrayList. Kaldet
     * sker i en tråd og observeren fyrer en firePropertyChange, når brugerne er hentet.
     */
    public void hentBrugere() {
        Query query = firestore.collection("brugere");

        Thread thread = new Thread(() -> {
            ArrayList<Bruger> brugere = new ArrayList<>();

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

            support.firePropertyChange("hentBrugere", null, brugere);
        });
        thread.start();
    }

    /**
     * Gemmer en chat i "chats" collection i databasen.
     * @param chat et chatobjekt skal først skabes.
     */
    public void opretChat(Chat chat) {
        firestore.collection("chats").document().create(chat);
    }

    /**
     * Der laves 2 queries. Der skabes et chatobjekt for hver reference i databasen, som passer til en af de to queries.
     * Chatobjekterne tilføjes til en ArrayList. Det hele sker i en thread og til sidst sker et observerkald, når
     * querien er udført.
     * @param navn navn kan både være afsender og modtager i en chat
     * @see ArrayList<Besked> hentBeskeder(DocumentReference reference) for at få beskederne til chatten
     */
    public void hentChatsMedNavn(String navn) {
        Query query1 = firestore.collection("chats").whereEqualTo("afsender", navn);
        Query query2 = firestore.collection("chats").whereEqualTo("modtager", navn);

        Thread thread = new Thread(() -> {
            ArrayList<Chat> chats = new ArrayList<>();

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

            /* Sorter listen*/
            Collections.sort(chats, Chat.ChatTidspunktComparator);

            support.firePropertyChange("hentChatsMedNavn", null, chats);
        });
        thread.start();
    }

    /**
     * Bruges til at hente de beskeder, som chatobjektet indeholder.
     * @param reference denne DocumentReference skabes af firebase, når vi querier databasen i en anden metode.
     * @see public void hentChatsMedNavn(String navn)
     * @return returnerer en arrayliste med beskedobjekter
     */
    private ArrayList<Besked> hentBeskeder(DocumentReference reference) {
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

    /**
     * Gemmer en chat i databasen. Der laves en query for at finde den specifikke chat i databasen.
     * @param chat den gamle værdi overskrives af det chatobjekt, der gives med som parameter
     */
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

    /**
     * @author Tommy
     * Kaldes fra UI'et for at liveopdatere beskeder i chatvinduet
     * @param chat den chat, der skal opdateres
     */
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

    /**
     * @author Benjamin
     */
    public void gemProgram(Traeningsprogram program) {
        String patientEmail = program.getPatientEmail();
        firestore.collection("træningsprogram").document(patientEmail).set(program);
    }

    public void hentProgrammer() {
        Query query = firestore.collection("træningsprogram");

        Thread thread = new Thread(() -> {
            ArrayList<Traeningsprogram> programmer = new ArrayList<>();

            try {
                QuerySnapshot querySnapshot = query.get().get();
                if (!querySnapshot.isEmpty()) {
                    for (int i = 0; i < querySnapshot.size(); i++) {
                        programmer.add(querySnapshot.getDocuments().get(i).toObject(Traeningsprogram.class));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            support.firePropertyChange("hentProgrammer", null, programmer);
        });
        thread.start();
    }

    public void gemOevelse(Oevelse oevelse) {
        String navn = oevelse.getNavn();
        firestore.collection("oevelser").document(navn).create(oevelse);
    }

    public void hentOevelser() {
        Query query = firestore.collection("øvelser");

        Thread thread = new Thread(() -> {
            ArrayList<Oevelse> oevelser = new ArrayList<>();

            try {
                QuerySnapshot querySnapshot = query.get().get();
                if (!querySnapshot.isEmpty()) {
                    for (int i = 0; i < querySnapshot.size(); i++) {
                        oevelser.add(querySnapshot.getDocuments().get(i).toObject(Oevelse.class));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            support.firePropertyChange("hentOevelser", null, oevelser);
        });
        thread.start();
    }

    public void hentBegivenheder() {
        Query query = firestore.collection("begivenheder");

        Thread thread = new Thread(() -> {
            ArrayList<Begivenhed> begivenheder = new ArrayList<>();

            try {
                QuerySnapshot querySnapshot = query.get().get();
                if (!querySnapshot.isEmpty()) {
                    for (int i = 0; i < querySnapshot.size(); i++) {
                        begivenheder.add(querySnapshot.getDocuments().get(i).toObject(Begivenhed.class));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            support.firePropertyChange("hentBegivenheder", null, begivenheder);
        });
        thread.start();
    }

    /**
     * Tilføjer en observer
     * @param listener den observer, som skal tilføjes
     */
    public void tilfoejObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}