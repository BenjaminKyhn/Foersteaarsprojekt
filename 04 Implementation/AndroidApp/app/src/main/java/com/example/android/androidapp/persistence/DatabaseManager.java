package com.example.android.androidapp.persistence;

import androidx.annotation.NonNull;

import com.example.android.androidapp.domain.Besked;
import com.example.android.androidapp.domain.Bruger;
import com.example.android.androidapp.domain.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.HashMap;

/** @author Tommy **/
public class DatabaseManager {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

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
                    DocumentReference reference = querySnapshot.getDocuments().get(0).getReference();
                    reference.set(updatedChat);
                    Besked sidsteBesked = chat.getBeskeder().get(chat.getBeskeder().size() - 1);
                    reference.collection("beskeder").document().set(sidsteBesked);
                }
            }
        });
    }
}
