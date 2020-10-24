package com.hippovio.child.database.firebase;

public class FirebaseServiceInterfaces {
    //callback interfaces

    public static interface connectivityFirebaseCallback {
        void onConnectedCallBack();
        void onNotConnectedCallBack();
    }

    public static interface writeFirebaseCallback {
        void onWriteCallBack(boolean writeSuccessful);
    }

    public static interface successfulOperationCallback {
        void onSuccess();
        void onFailure();
    }

}
