package com.example.android.androidapp.persistence;

import androidx.annotation.NonNull;

import com.example.android.androidapp.domain.Bruger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/** @author Tommy **/
public class DatabaseManager {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void gemBruger(Bruger bruger) {

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
}
