package com.hippovio.child.database.firebase.firestoreDao;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hippovio.child.database.firebase.FirebaseServiceInterfaces;
import com.hippovio.child.database.firebase.constants.CollectionNames;
import com.hippovio.child.database.local.entities.Chatee;

public class ChateeDao extends FirestoreDao<Chatee> {

    private DocumentReference getChildDocument() {
        //TODO: remove hardcoding
        return FirebaseFirestore.getInstance().collection(CollectionNames.userCollection)
                .document("dummy_child");
    }

    @Override
    protected CollectionReference getCollection() {
       return getChildDocument().collection(CollectionNames.chateeCollection);
    }

    @Override
    public void saveOne(final Chatee chatee, final FirebaseServiceInterfaces.writeFirebaseCallback writeFirebaseCallback) {
        chatee.setUserId(getChildDocument().getPath());
        super.saveOne(chatee, writeFirebaseCallback);
    }
}
