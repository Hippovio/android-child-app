package com.hippovio.child.database;

import android.content.Context;

import androidx.room.Room;

import com.hippovio.child.database.firebase.FirebaseHelper;
import com.hippovio.child.database.firebase.FirebaseServiceInterfaces;
import com.hippovio.child.database.local.HippovioDatabase;
import com.hippovio.child.database.local.entities.Chatee;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.database.local.entities.MessageSyncStatus;
import com.hippovio.child.enums.MessageSyncStates;
import com.hippovio.child.pojos.Message;

import java.util.Date;
import java.util.List;

public class DatabaseHelper {

    HippovioDatabase localDb;

    public DatabaseHelper() {
    }

    public DatabaseHelper(Context context) {
        localDb = Room.databaseBuilder(context, HippovioDatabase.class, "database-name").build();
    }

    public Chatee getLocalWhatsappChateeForSender(String phoneNumber){
        return localDb.chateeDao().getWhatsappChateeForSender(phoneNumber);
    }

    public List<MessageReadCheckpoint> getLocalMessageBreakPointsForChatee(Chatee chatee){
        return localDb.messageCheckpointsDao().getCheckpointsForChateeIdOrderedByLatest(chatee.getChateeId());
    }

    public void checkOnlineDbConnectivity(){
        FirebaseHelper.checkConnectivity();
    }

    public void uploadMessageOnline(final Message message){
        message.setId(FirebaseHelper.generateId(FirebaseHelper.MESSAGES_COLLECTION));
        localDb.messageSyncStatusDao().insertAll(new MessageSyncStatus(message.getId(), MessageSyncStates.NEW, new Date(System.currentTimeMillis())));
        FirebaseHelper.saveMessage(message, new FirebaseServiceInterfaces.successfulOperationCallback() {
            @Override
            public void onSuccess() {
                MessageSyncStatus currentMessageSyncStatus = localDb.messageSyncStatusDao().getMessageSycnStatus(message.getId());
                currentMessageSyncStatus.setSyncState(MessageSyncStates.UPLOADED);
                currentMessageSyncStatus.setLastModified(new Date(System.currentTimeMillis()));
                localDb.messageSyncStatusDao().updateMessageSyncStatus(currentMessageSyncStatus);
            }

            @Override
            public void onFailure() {

            }
        });
    }

}
