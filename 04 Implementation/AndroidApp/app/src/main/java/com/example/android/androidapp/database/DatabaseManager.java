package com.example.android.androidapp.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.androidapp.entities.Begivenhed;
import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.Oevelse;
import com.example.android.androidapp.entities.Traeningsprogram;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tommy
 **/
// Denne klasse instantierer firebase og håndterer alle databasekald i programmet.
public class DatabaseManager {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList<Chat> nuvaerendeChats;
    private boolean write;
    private PropertyChangeSupport support;

    public DatabaseManager() {
        support = new PropertyChangeSupport(this);
    }

    public void gemBruger(Bruger bruger) {
        firestore.collection("brugere").document(bruger.getEmail()).set(bruger);
    }

    public void gemChat(Chat chat) {
        firestore.collection("chats").document().set(chat);

        if (chat.hentBeskeder().size() > 0) {
            Query query = firestore.collection("chats").whereEqualTo("afsender", chat.getAfsender())
                    .whereEqualTo("modtager", chat.getModtager()).whereEqualTo("emne", chat.getEmne());
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (Besked besked : chat.hentBeskeder()) {
                        queryDocumentSnapshots.getDocuments().get(0).getReference().collection("beskeder").document().set(besked);
                    }
                }
            });
        }
    }

    public void sletBruger(Bruger bruger) {
        firestore.collection("brugere").document(bruger.getEmail()).delete();
    }

    public void hentBrugerMedEmail(String email) {
        firestore.collection("brugere").document(email).get().addOnSuccessListener(documentSnapshot -> {
            Bruger bruger = null;
            if (documentSnapshot.exists()) {
                bruger = documentSnapshot.toObject(Bruger.class);
            }
            support.firePropertyChange("hentBrugerMedEmail", null, bruger);
        });
    }

    public void hentBehandlereTilBruger(Bruger bruger, List<Bruger> brugere) {
        firestore.collection("brugere").document(bruger.getEmail()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Bruger patient = documentSnapshot.toObject(Bruger.class);
                assert patient != null;
                ArrayList<String> behandlere = patient.getBehandlere();
                if (behandlere.size() != 0) {
                    for (String behandler : behandlere) {
                        firestore.collection("brugere").whereEqualTo("navn", behandler)
                                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            Bruger brugerFraDB = queryDocumentSnapshots.getDocuments().get(0).toObject(Bruger.class);
                            brugere.add(brugerFraDB);
                        });
                    }

                }
            }
        });
    }

    public void opdaterChat(final Chat chat) {
        write = true;
        Query query = firestore.collection("chats").whereEqualTo("afsender", chat.getAfsender()).whereEqualTo("modtager", chat.getModtager())
                .whereEqualTo("emne", chat.getEmne());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                assert querySnapshot != null;
                if (!querySnapshot.isEmpty()) {
                    HashMap<String, Object> updatedChat = new HashMap<>();
                    updatedChat.put("afsender", chat.getAfsender());
                    updatedChat.put("modtager", chat.getModtager());
                    updatedChat.put("emne", chat.getEmne());
                    long nu = System.currentTimeMillis();
                    updatedChat.put("sidstAktiv", nu);
                    DocumentReference reference = querySnapshot.getDocumentChanges().get(0).getDocument().getReference();
                    reference.set(updatedChat);
                    Besked sidsteBesked = chat.hentBeskeder().get(chat.hentBeskeder().size() - 1);
                    reference.collection("beskeder").document().set(sidsteBesked);
                }
            }
        });
    }

    public void hentChatsTilBruger(String navn, ArrayList<Chat> chats) {
        nuvaerendeChats = chats;
        Query query1 = firestore.collection("chats").whereEqualTo("afsender", navn);
        Query query2 = firestore.collection("chats").whereEqualTo("modtager", navn);

        query1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Chat> temp = queryDocumentSnapshots.toObjects(Chat.class);
                for (int i = 0; i < queryDocumentSnapshots.getDocumentChanges().size(); i++) {
                    final Chat chat = temp.get(i);
                    DocumentReference documentReference = queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().getReference();
                    documentReference.collection("beskeder").orderBy("tidspunkt").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Besked> beskeder = queryDocumentSnapshots.toObjects(Besked.class);
                            ArrayList<Besked> arrayList = new ArrayList<>(beskeder);
                            chat.setBeskeder(arrayList);
                            nuvaerendeChats.add(chat);
                            Collections.sort(nuvaerendeChats, Chat.sorterVedSidstAktiv);
                        }
                    });
                }
            }
        });

        query2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Chat> temp = queryDocumentSnapshots.toObjects(Chat.class);
                for (int i = 0; i < queryDocumentSnapshots.getDocumentChanges().size(); i++) {
                    final Chat chat = temp.get(i);
                    DocumentReference documentReference = queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().getReference();
                    documentReference.collection("beskeder").orderBy("tidspunkt").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Besked> beskeder = queryDocumentSnapshots.toObjects(Besked.class);
                            chat.setBeskeder(new ArrayList<>(beskeder));
                            nuvaerendeChats.add(chat);
                            Collections.sort(nuvaerendeChats, Chat.sorterVedSidstAktiv);
                        }
                    });
                }
            }
        });

    }

    public void observerBeskederFraFirestore(final Chat chat) {
        String afsender = chat.getAfsender();
        String modtager = chat.getModtager();
        String emne = chat.getEmne();

        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager)
                .whereEqualTo("emne", emne);

        Source source = Source.SERVER;
        query.get(source).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Query subQuery = queryDocumentSnapshots.getDocuments().get(0).getReference().collection("beskeder").orderBy("tidspunkt");
                    subQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (!write) {
                                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    if (list.size() != chat.hentBeskeder().size()) {
                                        Besked besked = list.get(list.size() - 1).toObject(Besked.class);
                                        chat.tilfoejBesked(besked);
                                    }
                                }
                            } else {
                                write = false;
                            }
                        }
                    });
                }
            }
        });
    }

    public void hentProgramTilBruger(Bruger bruger) {
        firestore.collection("træningsprogram").document(bruger.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    if (documentSnapshot.getData() != null) {
                        Traeningsprogram traeningsprogram = documentSnapshot.toObject(Traeningsprogram.class);
                        ArrayList<Traeningsprogram> traeningsprogramArrayList = new ArrayList<>();
                        traeningsprogramArrayList.add(traeningsprogram);
                        support.firePropertyChange("hentProgramTilBruger", null, traeningsprogramArrayList);
                    }
                }
            }
        });
    }

    public void hentOevelser() {
        firestore.collection("øvelser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        List<Oevelse> list = querySnapshot.toObjects(Oevelse.class);
                        ArrayList<Oevelse> arrayList = new ArrayList<>(list);
                        support.firePropertyChange("hentOevelser", null, arrayList);
                    }
                }
            }
        });
    }

    public void hentBegivenhederTilBruger(Bruger bruger) {
        ArrayList<String> navneTilQuery = new ArrayList<>();
        navneTilQuery.add(bruger.getNavn());
        navneTilQuery.addAll(bruger.getBehandlere());
        if (navneTilQuery.size() > 10) {
            System.out.println("For mange navne. Firestore støtter kun ti strings til query whereArrayContainsAny");
            return;
        }
        Query query = firestore.collection("begivenheder").whereArrayContainsAny("deltagere", navneTilQuery);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        List<Begivenhed> list = querySnapshot.toObjects(Begivenhed.class);
                        ArrayList<Begivenhed> arrayList = new ArrayList<>(list);
                        support.firePropertyChange("hentBegivenhederTilBruger", null, arrayList);
                    }
                }
            }
        });
    }

    public void tilfoejListener(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    public void fjernListener(PropertyChangeListener propertyChangeListener) {
        support.removePropertyChangeListener(propertyChangeListener);
    }
}
