package com.hippovio.child.database.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hippovio.child.pojos.Message;

public class FirebaseHelper {

    public static final String MESSAGES_COLLECTION = "messages";

    public static String generateId(String collectionPath){
        return FirebaseFirestore.getInstance().collection(collectionPath).document().getId();
    }

    public static void saveMessage(Message message, final FirebaseServiceInterfaces.successfulOperationCallback successfulOperationCallback){
        FirestoreService.saveMessage(message, new FirebaseServiceInterfaces.writeFirebaseCallback() {
            @Override
            public void onWriteCallBack(boolean writeSuccessful) {
                Log.i("Firebase", "Message written");
                if (writeSuccessful) {
                    successfulOperationCallback.onSuccess();
                } else {
                    successfulOperationCallback.onFailure();
                }
            }
        });
    }

    public static void checkConnectivity(){
        RealtimeDatabaseService.checkConnected(new FirebaseServiceInterfaces.connectivityFirebaseCallback() {
            @Override
            public void onConnectedCallBack() {
                Log.i("Firebase", "connected");
            }

            @Override
            public void onNotConnectedCallBack() {
                Log.i("Firebase", "no connectivity");
            }
        });
    }

}
