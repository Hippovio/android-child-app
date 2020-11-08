package com.hippovio.child.services.messageRead;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.hippovio.child.database.MessageDatabaseHelper;
import com.hippovio.child.database.local.entities.Chatee;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.pojos.Message;
import com.hippovio.child.services.messageRead.helpers.MessageHelper;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MessageReadService {

    protected String LOG_TAG;
    protected Context context;
    protected MessageDatabaseHelper messageDatabaseHelper;
    protected Chatee chatee;

    /**
     * Any Event for a particular Package
     * @param accessibilityEvent event
     * @param rootInActiveWindow root
     * @param context app context
     */
    public abstract void appEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfoCompat rootInActiveWindow, Context context);

    /**
     * Reads and saves the chat in current window
     * @param accessibilityEvent
     */
    protected void readChat(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo source = accessibilityEvent.getSource();

        LinkedList<Message> messages = readMessages(source);

        insertChat(messages);
    }

    /**
     * Reads the messages in the window.
     * @param source
     * @return List of messages read
     */
    protected LinkedList<Message> readMessages(AccessibilityNodeInfo source) {
        LinkedList<Message> messages = new LinkedList<>();
        if (source.getChildCount() > 0) {
            for(int index = 0; index < source.getChildCount(); index++){
                AccessibilityNodeInfo nodeInfo = source.getChild(index);
                if(nodeInfo != null) {
                    Message message = extractMessage(nodeInfo);
                    if (message != null)
                        messages.add(message);
                }
            }
        }
        return messages;
    }

    /**
     * Extracts message from a view
     * @param groupView view for the message
     * @return {@link Message} message and null if not a message
     */
    protected abstract Message extractMessage(AccessibilityNodeInfo groupView);

    /**
     * Sets Chatee from root window.
     * @param conversationRoot
     * @return chatee name.
     */
    protected abstract void extractChatee(AccessibilityNodeInfo conversationRoot);

    protected abstract boolean isMessageReceived(AccessibilityNodeInfo nodeInfo);

    protected abstract String getMessageText(AccessibilityNodeInfo nodeInfo,  boolean isMessageReceived);

    /**
     * Sets the date in current scroll window.
     * @param nodeInfo
     */
    protected abstract void setDateInCurrentScroll (AccessibilityNodeInfo nodeInfo);

    /**
     * get Date from the date string.
     * @param dateString
     * @return {@link Date}
     */
    protected abstract Date getDate(String dateString);

    /**
     * Finds the insertion point and saves messages and updates the checkpoints.
     * @param messages
     */
    protected void insertChat(List<Message> messages) {

        List<Message> messagesWithDate = messages.stream().filter(message -> message.getDateTime() != null).collect(Collectors.toList());
        List<MessageReadCheckpoint> messageCheckpoints = messageDatabaseHelper.getLocalMessageBreakPointsForChatee(chatee);

        for(MessageReadCheckpoint checkpoint : messageCheckpoints){

            Pair<Integer, Integer> boundaryIndex = MessageHelper.findMessageOverlap(checkpoint, messages);

            if (boundaryIndex.first != -1 && boundaryIndex.second != -1) {
                Message boundaryMessage = messages.get(boundaryIndex.first);
                messages = messages.subList(boundaryIndex.first + 1, boundaryIndex.second);
                messages = MessageHelper.updateDate(messages, boundaryMessage.getDate());
                messages = messageDatabaseHelper.uploadMessagesOnline(messages);

                messageDatabaseHelper.deleteCheckpoint(checkpoint);
                //TODO: merge next checkpoint if that also overlaps
            } else if (boundaryIndex.first != -1) {
                Message boundaryMessage = messages.get(boundaryIndex.first);
                //messages after this index are of interest
                messages = messages.subList(boundaryIndex.first + 1, messages.size());
                messages = MessageHelper.updateDate(messages, boundaryMessage.getDate());
                messages = messageDatabaseHelper.uploadMessagesOnline(messages);

                checkpoint.updateStartMessage(messages.get(messages.size() - 1));
                messageDatabaseHelper.updateCheckpoint(checkpoint);
            } else if (boundaryIndex.second != -1){
                Message boundaryMessage = messages.get(boundaryIndex.second);
                //messages before this index are of interest
                messages = messages.subList(0, boundaryIndex.second);
                messages = messages.stream().filter(message -> message.getDateTime() != null).collect(Collectors.toList());
                messages = messageDatabaseHelper.uploadMessagesOnline(messages);

                checkpoint.updateEndMessage(messages.get(0));
                messageDatabaseHelper.updateCheckpoint(checkpoint);
            } else {
                // No Overlap Case.
                MessageReadCheckpoint newMessageWindow = MessageReadCheckpoint.MessageCheckpointsBuilder()
                        .startMessage(messagesWithDate.get(0))
                        .endMessage(messagesWithDate.get(messagesWithDate.size() - 1))
                        .build();

                if(checkpoint.hasInBetweenIt(newMessageWindow)){
                    //Considering only messages with date.
                    messages = messagesWithDate;
                    messages = messageDatabaseHelper.uploadMessagesOnline(messages);

                    MessageReadCheckpoint newCheckpoint = checkpoint.clone();
                    newCheckpoint.updateStartMessage(messagesWithDate.get(messages.size() - 1));

                    checkpoint.updateEndMessage(messages.get(0));

                    messageDatabaseHelper.updateAndCreateCheckpoint(checkpoint, newCheckpoint);
                } else
                    continue;
            }
            break;
        }
        Log.d(LOG_TAG, "Saved: " + messages);
    }

}
