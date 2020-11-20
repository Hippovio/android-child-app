package com.hippovio.child.database.firebase.firestoreDao;

import com.google.firebase.firestore.CollectionReference;
import com.hippovio.child.database.firebase.FirebaseServiceInterfaces;
import com.hippovio.child.database.local.entities.Entity;
import io.reactivex.Observable;
import static com.hippovio.child.database.firebase.FirestoreService.writeObjectToDocumentRef;

public abstract class FirestoreDao<T extends Entity> {

    /**
     * Get Collection Reference
     * @return {@link CollectionReference}
     */
     protected abstract CollectionReference getCollection();

    /**
     * Save one entity
     * @param entity entityToSave
     */
    public void saveOne(final T entity, final FirebaseServiceInterfaces.writeFirebaseCallback writeFirebaseCallback) {
        writeObjectToDocumentRef(getCollection().document(entity.getId().toString()), entity, writeFirebaseCallback);
    }

    public void saveList(final FirebaseServiceInterfaces.writeFirebaseCallback writeFirebaseCallback, final T... entities) {
        Observable.fromArray(entities).subscribe(
                entity -> saveOne(entity, writeSuccessful -> {}),
                error -> writeFirebaseCallback.onWriteCallBack(false),
                () -> writeFirebaseCallback.onWriteCallBack(true)
        );
    }

}
