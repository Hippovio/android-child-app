package com.hippovio.child.whatsapp.service;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.services.messageRead.helpers.AccessibilityHelper;
import com.hippovio.child.database.MessageDatabaseHelper;
import com.hippovio.child.pojos.Message;
import com.hippovio.child.services.messageRead.MessageReadService;
import com.hippovio.child.services.messageRead.helpers.MessageHelper;
import com.hippovio.child.whatsapp.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
            AccessibilityNodeInfo rootNode = AccessibilityHelper.getNodeInfo(accessibilityEvent.getSource(), Constants.conversationContactNameId);
            if (rootNode == null) {
                chatee = null;
                return;
            }
            extractChatee(rootNode);
            areUnreadMessages = false;
        } else
            return;
    }

    @Override
    protected void readChat(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo source = accessibilityEvent.getSource();

        LinkedList<Message> messages = readMessages(source);

        insertChat(messages);
    }

    @Override
    protected Message extractMessage(AccessibilityNodeInfo groupView) {
        if (groupView.getChildCount() == 0)
            return null;
        if (groupView.getChildCount() == 1) {
            if (isUnreadTag(groupView)) {
                areUnreadMessages = true;
                // Since unread message therefore date will be today unless a date tag is found.
                //TODO: Consider scroll down
                WhatsAppReadService.dateInCurrentScroll = new Date();
            }
            return null;
        }

        setDateInCurrentScroll(groupView);
        boolean isMessageReceived = isMessageReceived(groupView);
        String timeText = getTimeText(groupView, isMessageReceived);
        String msgText = getMessageText(groupView, isMessageReceived);

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
    protected void extractChatee(AccessibilityNodeInfo conversationRoot) {
        try {
            String chateeName = conversationRoot.getText().toString();

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

            if(chatee == null){
                //create new chatee
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    protected Date getDate(String dateString) {
        try{
            if (dateString.equals("TODAY")) {
                Calendar cal = Calendar.getInstance();
                int year  = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date  = cal.get(Calendar.DATE);
                cal.clear();
                cal.set(year, month, date);
                return cal.getTime();
            } else if (dateString.equals("YESTERDAY")) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                int year  = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date  = cal.get(Calendar.DATE);
                cal.clear();
                cal.set(year, month, date);
                return cal.getTime();
            }
            DateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            Date date = format.parse(dateString);
            return date;
        }catch (Exception e) {}
        return null;
    }

    @Override
    protected void insertChat(List<Message> messages) {
        Log.d(LOG_TAG, "Received: " + messages);

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

    @Override
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



}
