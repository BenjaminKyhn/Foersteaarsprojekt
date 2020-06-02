package com.example.android.androidapp.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.androidapp.entities.Besked;
import com.example.android.androidapp.entities.Bruger;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.usecases.ObserverbarListe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tommy
 **/
public class DatabaseManager {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ObserverbarListe<Chat> nuvaerendeChats;
    private boolean read;
    private boolean write;

    public void gemBruger(Bruger bruger) {
        firestore.collection("brugere").document(bruger.getEmail()).set(bruger);
    }

    public void gemChat(Chat chat) {
        HashMap<String, Object> gemtData = new HashMap<>();
        gemtData.put("afsender", chat.getAfsender());
        gemtData.put("modtager", chat.getModtager());
        gemtData.put("emne", chat.getEmne());

        firestore.collection("chats").document().set(chat);
    }

    public CollectionReference hentBrugereReference() {
        return firestore.collection("brugere");
    }

    public CollectionReference hentChatsReference() {
        return firestore.collection("chats");
    }

    public void sletBruger(Bruger bruger) {
        firestore.collection("brugere").document(bruger.getEmail()).delete();
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
                    Besked sidsteBesked = chat.getBeskeder().get(chat.getBeskeder().size() - 1);
                    reference.collection("beskeder").document().set(sidsteBesked);
                }
            }
        });
    }

    public void hentChatsTilBruger(String navn, List<Chat> chats) {
        nuvaerendeChats = (ObserverbarListe<Chat>) chats;
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
                            ObserverbarListe<Besked> observerbarListe = new ObserverbarListe<>();
                            observerbarListe.addAll(beskeder);
                            chat.setBeskeder(observerbarListe);
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
                                    if (list.size() != chat.getBeskeder().size()) {
                                        Besked besked = list.get(list.size() - 1).toObject(Besked.class);
                                        chat.tilfoejBesked(besked);
                                    }
                                }
                            }
                            else {
                                write = false;
                            }
                        }
                    });
                }
            }
        });
    }

    public void hentProgram(Bruger bruger, final ObserverbarListe<String> program) {
        firestore.collection("træningsprogram").document(bruger.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    if (documentSnapshot.getData() != null) {
                        for (Object item : documentSnapshot.getData().values()) {
                            program.add(item.toString().substring(1, item.toString().length() - 1));
                        }
                    }
                }
            }
        });
    }
}