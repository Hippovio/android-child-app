package com.hippovio.child.database;

import android.content.Context;

import androidx.room.Room;

import com.hippovio.child.database.callbacks.DatabaseCallbacks;
import com.hippovio.child.database.firebase.FirebaseHelper;
import com.hippovio.child.database.firebase.FirebaseServiceInterfaces;
import com.hippovio.child.database.local.HippovioDatabase;
import com.hippovio.child.database.local.entities.Chatee;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.database.local.entities.MessageSyncStatus;
import com.hippovio.child.enums.MessageSyncStates;
import com.hippovio.child.helpers.AsyncHelper;
import com.hippovio.child.pojos.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Database helper for offline/online messages.
 */
public class MessageDatabaseHelper {

    HippovioDatabase localDb;

    public MessageDatabaseHelper() {
    }

    public MessageDatabaseHelper(Context context) {
        localDb = Room.databaseBuilder(context, HippovioDatabase.class, "database-name").build();
    }

    /**
     * LOCAL DB METHODS
     */

    public void getLocalWhatsappChateeForSender(String phoneNumber, Consumer<Chatee> onSuccessCallback, Consumer<Throwable> onErrorCallback){
        new AsyncHelper<Chatee>().asyncForSingle(localDb.chateeDao().getWhatsappChateeForSender(phoneNumber), onSuccessCallback, onErrorCallback);
    }

    public void createAndSaveNewChattee(Chatee newChatee, DatabaseCallbacks.chateeIdCallback chateeIdCallback){
        new AsyncHelper<Long>().asyncForSingle(localDb.chateeDao().insert(newChatee), insertedChatteeId -> {
            MessageReadCheckpoint newChatteeMessageCheckpoint = new MessageReadCheckpoint();
            newChatteeMessageCheckpoint.setChateeId(insertedChatteeId);
            newChatteeMessageCheckpoint.setSource(newChatee.getChateeSource());
            Calendar startMessageDate = Calendar.getInstance();
            startMessageDate.add(Calendar.DAY_OF_MONTH, -7);
            newChatteeMessageCheckpoint.setStartMessageDate(startMessageDate.getTime());
            new AsyncHelper<List<Long>>().asyncForSingle(localDb.messageCheckpointsDao().insertAll(newChatteeMessageCheckpoint), insertedCheckpointIds -> {});
            chateeIdCallback.onChateeId(insertedChatteeId);
        });
    }


    public List<MessageReadCheckpoint> getLocalMessageBreakPointsForChatee(Chatee chatee){
        return localDb.messageCheckpointsDao().getCheckpointsForChateeIdOrderedByLatest(chatee.getChateeId().toString());
    }

    public void deleteCheckpoint(MessageReadCheckpoint checkpoint){
        localDb.messageCheckpointsDao().delete(checkpoint);
    }

    public void updateCheckpoint(MessageReadCheckpoint checkpoint){
        localDb.messageCheckpointsDao().updateMessageReadCheckpoint(checkpoint);
    }

    public void updateAndCreateCheckpoint(MessageReadCheckpoint update, MessageReadCheckpoint create){
        localDb.messageCheckpointsDao().updateAndCreateCheckpoint(update, create);
    }


    /**
     * ONLINE FIREBASE DB METHODS
     */

    public void checkOnlineDbConnectivity(){
        FirebaseHelper.checkConnectivity();
    }

    public Message uploadMessageOnline(final Message message){
        message.setId(FirebaseHelper.generateId(FirebaseHelper.MESSAGES_COLLECTION));
        localDb.messageSyncStatusDao().insertAll(new MessageSyncStatus(message.getId(), MessageSyncStates.NEW,
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
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
        return message;
    }

    public List<Message> uploadMessagesOnline(final List<Message> messages){
        List<Message> uploadedMessages = new ArrayList<>();
        messages.forEach(message -> uploadedMessages.add(uploadMessageOnline(message)));
        return uploadedMessages;
    }

}
