package com.hippovio.child.database.firebase.firestoreDao;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hippovio.child.database.firebase.FirebaseServiceInterfaces;
import com.hippovio.child.database.firebase.constants.CollectionNames;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;

public class CheckpointsDao extends FirestoreDao<MessageReadCheckpoint> {

    private DocumentReference getChildDocument() {
        //TODO: remove hardcoding
        return FirebaseFirestore.getInstance().collection(CollectionNames.userCollection)
                .document("dummy_child");
    }

    @Override
    protected CollectionReference getCollection() {
        return getChildDocument().collection(CollectionNames.checkpointCollection);
    }

    @Override
    public void saveOne(final MessageReadCheckpoint checkpoint, final FirebaseServiceInterfaces.writeFirebaseCallback writeFirebaseCallback) {
        checkpoint.setUserId(getChildDocument().getPath());
        super.saveOne(checkpoint, writeFirebaseCallback);
    }

}
