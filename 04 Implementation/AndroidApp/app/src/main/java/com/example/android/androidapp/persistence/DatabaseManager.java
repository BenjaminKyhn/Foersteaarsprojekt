package com.example.android.androidapp.persistence;

import androidx.annotation.NonNull;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.domain.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/** @author Tommy **/
public class DatabaseManager {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void gemBruger(Bruger bruger) {
        firestore.collection("brugere").document(bruger.getEmail()).set(bruger);
    }

    public Bruger hentBrugerMedEmail(String email) {
        final Bruger[] bruger = {null};
        firestore.collection("brugere").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    bruger[0] = document.toObject(Bruger.class);
                }
            }
        });
        return bruger[0];
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
                    querySnapshot.getDocuments().get(0).getReference().set(chat);
                }
            }
        });
    }

    public Chat hentChat(String afsender, String modtager, String emne) {
        final Chat[] chat = {null};
        Query query = firestore.collection("chats").whereEqualTo("afsender", afsender).whereEqualTo("modtager", modtager).whereEqualTo("emne", emne);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                assert querySnapshot != null;
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    chat[0] = documentSnapshot.toObject(Chat.class);
                    assert chat[0] != null;
                    chat[0].setBeskeder(hentBeskeder(documentSnapshot.getReference()));
                }
            }
        });
        return chat[0];
    }

    private ArrayList<Besked> hentBeskeder(DocumentReference reference) {
        final ArrayList<Besked> beskeder = new ArrayList<>();
        reference.collection("beskeder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                assert querySnapshot != null;
                if (!querySnapshot.isEmpty()) {
                    List<DocumentSnapshot> list = querySnapshot.getDocuments();
                    for (int i = 0; i < list.size(); i++) {
                        Besked besked = list.get(i).toObject(Besked.class);
                        beskeder.add(besked);
                    }
                }
            }
        });
        return beskeder;
    }
}
