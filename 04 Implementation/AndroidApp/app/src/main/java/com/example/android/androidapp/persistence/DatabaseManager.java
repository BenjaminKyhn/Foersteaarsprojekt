package com.example.android.androidapp.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.domain.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** @author Tommy **/
public class DatabaseManager {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList<Besked> observeretBeskeder;

    public void gemBruger(Bruger bruger) {
        firestore.collection("brugere").document(bruger.getEmail()).set(bruger);
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
                    String nu = new Timestamp(System.currentTimeMillis()).toString();
                    updatedChat.put("sidstAktiv", nu);
                    DocumentReference reference = querySnapshot.getDocumentChanges().get(0).getDocument().getReference();
                    reference.set(updatedChat);
                    Besked sidsteBesked = chat.getBeskeder().get(chat.getBeskeder().size() - 1);
                    reference.collection("beskeder").document().set(sidsteBesked);
                }
            }
        });
    }

    public void observerChat(final Chat chat) {
        chat.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("nyBesked")) {
                    opdaterChat(chat);
                }
            }
        });
    }

    public void observerBeskederFraFirestore(Chat chat, ArrayList<Besked> beskeder) {
        String afsender = chat.getAfsender();
        String modtager = chat.getModtager();
        String emne = chat.getEmne();
        final int chatBeskedStoerrelse = chat.getBeskeder().size();
        observeretBeskeder = beskeder;

        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager)
                .whereEqualTo("emne", emne);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    CollectionReference collectionReference = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().getReference().collection("beskeder");
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                if (queryDocumentSnapshots.getDocumentChanges().size() != chatBeskedStoerrelse) {
                                    List<Besked> beskeder = queryDocumentSnapshots.toObjects(Besked.class);
                                    Besked nyesteBesked = beskeder.get(beskeder.size() - 1);
                                    observeretBeskeder.add(nyesteBesked);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
