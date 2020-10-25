package com.hippovio.child.database.firebase;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hippovio.child.pojos.Message;

public class FirestoreService {

    public static void writeObjectToDocumentRef(final DocumentReference documentReference,
                                                final Object object, final FirebaseServiceInterfaces.writeFirebaseCallback writeFirebaseCallback){
        documentReference.set(object).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                writeFirebaseCallback.onWriteCallBack(true);
            }
        });
    }

    public static void saveMessage(Message message, FirebaseServiceInterfaces.writeFirebaseCallback writeFirebaseCallback){
        writeObjectToDocumentRef(FirebaseFirestore.getInstance().collection("messages")
                .document(message.getId()), message, writeFirebaseCallback);

    }
}
