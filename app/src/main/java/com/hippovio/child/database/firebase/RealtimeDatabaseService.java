package com.hippovio.child.database.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeDatabaseService {

    public static void checkConnected(final FirebaseServiceInterfaces.connectivityFirebaseCallback connectivityFirebaseCallback){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    connectivityFirebaseCallback.onConnectedCallBack();
                }
                else{
                    connectivityFirebaseCallback.onNotConnectedCallBack();
                    Log.i("Firebase", "no connectivity");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }


}
