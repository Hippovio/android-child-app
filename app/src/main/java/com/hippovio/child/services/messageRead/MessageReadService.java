package com.hippovio.child.services.messageRead;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.hippovio.child.database.MessageDatabaseHelper;
import com.hippovio.child.database.local.entities.Chatee;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.helpers.AsyncHelper;
import com.hippovio.child.pojos.Message;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class MessageReadService {

    protected String LOG_TAG;
    protected Context context;
    protected MessageDatabaseHelper messageDatabaseHelper;
    protected Chatee chatee;
    protected List<MessageReadCheckpoint> checkpoints;

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
    protected  abstract void readChat(AccessibilityEvent accessibilityEvent);

    /**
     * Reads the messages in the window.
     * @param source
     * @return List of messages read
     */
    protected abstract LinkedList<Message> readMessages(AccessibilityNodeInfo source);

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

    protected abstract String getTimeText(AccessibilityNodeInfo nodeInfo,  boolean isMessageReceived);

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
    protected abstract void insertChat(List<Message> messages);

    protected void createNewChatee(Chatee newChatee) {
        messageDatabaseHelper.createAndSaveNewChattee(newChatee, new AsyncHelper.CallBack<Long>() {
            @Override
            public void onSuccess(Long chateeId) {
                newChatee.setChateeId(chateeId);
                chatee = newChatee;
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    protected List<MessageReadCheckpoint> getCheckpoints() {
        if (checkpoints == null) {
            checkpoints = messageDatabaseHelper.getLocalMessageBreakPointsForChatee(chatee);
        }
        return checkpoints;
    }
}
