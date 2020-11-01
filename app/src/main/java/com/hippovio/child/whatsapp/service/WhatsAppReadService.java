package com.hippovio.child.whatsapp.service;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.hippovio.child.services.messageRead.helpers.AccessibilityHelper;
import com.hippovio.child.database.MessageDatabaseHelper;
import com.hippovio.child.pojos.Message;
import com.hippovio.child.services.messageRead.MessageReadService;
import com.hippovio.child.whatsapp.Constants;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import static com.hippovio.child.enums.Sources.WHATSAPP;

public class WhatsAppReadService extends MessageReadService {

    private static Date dateInCurrentScroll = null;
    private boolean areUnreadMessages = false;


    public WhatsAppReadService() {
        LOG_TAG = WHATSAPP.value();
    }

    @Override
    public void appEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfoCompat rootInActiveWindow, Context context) {

        this.context = context;
        messageDatabaseHelper = new MessageDatabaseHelper(context);

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (chatee == null)
                return;
            readChat(accessibilityEvent);

        } else if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfoCompat rootNode = AccessibilityHelper.getNodeInfo(rootInActiveWindow, Constants.editTextViewId);
            if (rootNode == null) {
                chatee = null;
                return;
            }
            areUnreadMessages = false;
        } else
            return;
    }

    @Override
    protected Message extractMessage(AccessibilityNodeInfo groupView) {
        if (groupView.getChildCount() == 0)
            return null;
        if (groupView.getChildCount() == 1) {
            if (isUnreadTag(groupView)) {
                areUnreadMessages = true;
                // Since unread message therefore date will be today unless a tag is found.
                WhatsAppReadService.dateInCurrentScroll = new Date();
            }
            return null;
        }

        boolean isMessageReceived = isMessageReceived(groupView);
        String timeText = getTimeText(groupView, isMessageReceived);
        String msgText = getMessageText(groupView, isMessageReceived);
        setDateInCurrentScroll(groupView);

        // Ignoring if other views
        if (timeText.isEmpty() || msgText.isEmpty() || timeText.indexOf(':') == -1)
            return null;

        return Message.MessageBuilder().chatee(chatee).msg(msgText).isReceived(isMessageReceived)
                .timeText(timeText).date(WhatsAppReadService.dateInCurrentScroll).isUnread(areUnreadMessages).build();
    }

    @Override
    protected boolean isMessageReceived(AccessibilityNodeInfo nodeInfo) {
        final String[] sentMessageDesciption = {"Delivered", "Read", "Sent"};
        try{
            return Arrays.binarySearch(sentMessageDesciption, nodeInfo.getChild(nodeInfo.getChildCount() - 1).getContentDescription().toString()) == -1;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    protected String getMessageText(AccessibilityNodeInfo nodeInfo,  boolean isMessageReceived) {
        try{
            return (isMessageReceived ? nodeInfo.getChild(nodeInfo.getChildCount() - 2).getText().toString() : nodeInfo.getChild(nodeInfo.getChildCount() - 3).getText().toString());
        }catch (Exception e) {
            return "";
        }
    }

    @Override
    protected String getTimeText(AccessibilityNodeInfo nodeInfo,  boolean isMessageReceived) {
        try{
            return (isMessageReceived ? nodeInfo.getChild(nodeInfo.getChildCount() - 1).getText() : nodeInfo.getChild(nodeInfo.getChildCount() - 2).getText()).toString();
        }catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void extractChatee(AccessibilityNodeInfoCompat conversationRoot) {
        try {
            String chateeName =  (String) conversationRoot.getChild(1).getChild(0).getText();

            String phoneNumber = null;
            String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + chateeName +"%'";
            String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, selection, null, null);
            if (c.moveToFirst()) {
                phoneNumber = c.getString(0);
            }
            c.close();
            if(phoneNumber == null) {
                Log.i(LOG_TAG, "Chatee Number not found");
            }
            else
                chatee = messageDatabaseHelper.getLocalWhatsappChateeForSender(phoneNumber);
        } catch (Exception e) {}
    }

    private boolean isUnreadTag(AccessibilityNodeInfo nodeInfo) {
        try{
            AccessibilityNodeInfo textView = nodeInfo.getChild(0);
            if (textView != null){
                String text = textView.getText().toString();
                return (!text.isEmpty() && text.contains(" UNREAD MESSAGE"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void setDateInCurrentScroll(AccessibilityNodeInfo nodeInfo) {
        try{
            Date date = getDate(nodeInfo.getChild(0).getText().toString());
            if (date != null)
                WhatsAppReadService.dateInCurrentScroll = date;
        } catch (Exception e){}
    }

}
