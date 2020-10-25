package com.hippovio.child.whatsapp.service;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.hippovio.child.AccessibilityHelper;
import com.hippovio.child.database.DatabaseHelper;
import com.hippovio.child.database.local.entities.Chatee;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.hashing.HashingService;
import com.hippovio.child.pojos.Message;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WhatsAppReadService {

    private String chateeName;
    private String chateeNumber;
    Chatee chatee;
    private String TAG = "WHATSAPP";
    private String editTextViewId = "com.whatsapp:id/entry";
    private static Map<String, LinkedList<Message>> chats = new HashMap<>();

    private static Long dateInCurrentScroll = 0L;
    private boolean areUnreadMessages = false;

    Context context;

    DatabaseHelper databaseHelper;


    public void whatsAppEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfoCompat rootInActiveWindow, Context context) {

        this.context = context;

        databaseHelper = new DatabaseHelper(context);

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (chateeName == null)
                return;
            readChat(accessibilityEvent);

        } else if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfoCompat rootNode = AccessibilityHelper.getNodeInfo(rootInActiveWindow, editTextViewId);
            if (rootNode == null) {
                chateeName = null;
                return;
            }
            chateeName = getWhatsAppChateeName(rootInActiveWindow);
            chateeNumber = getChateeNumberFromName(chateeName);
            if(chateeNumber != null || chateeNumber != "Unsaved") {
                chatee = databaseHelper.getLocalWhatsappChateeForSender(chateeNumber);
            }
            else{
                Log.i(TAG, "Chatee Number not found");
            }
            if (!chats.containsKey(chateeName)) {
                chats.put(chateeName, new LinkedList<Message>());
            }
            areUnreadMessages = false;
        } else
            return;
    }

    private String getChateeNumberFromName(String chateeName) {
        String phoneNumber = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + chateeName +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            phoneNumber = c.getString(0);
        }
        c.close();
        if(phoneNumber==null)
            phoneNumber = "Unsaved";
        return phoneNumber;
    }

    private void readChat(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo source = accessibilityEvent.getSource();

        LinkedList<Message> messages = readMessages(source);

        insertChat(messages);

    }

    private LinkedList<Message> readMessages(AccessibilityNodeInfo source) {
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

    private Message extractMessage(AccessibilityNodeInfo chatView) {
        if (chatView.getChildCount() == 0)
            return null;
        if (chatView.getChildCount() == 1) {
            if (isUnreadTag(chatView)) {
                areUnreadMessages = true;
            }
            return null;
        }

        boolean isMessageReceived = isMessageReceived(chatView);
        String timeText = getTimeText(chatView, isMessageReceived);
        String msgText = getMessageText(chatView, isMessageReceived);
        setDate(chatView);
        Long messageTime = getMessageTime(timeText, WhatsAppReadService.dateInCurrentScroll);

        // Ignoring if other views
        if (timeText.isEmpty() || msgText.isEmpty() || timeText.indexOf(':') == -1)
            return null;

        return new Message(chatee, msgText,isMessageReceived, messageTime, timeText, areUnreadMessages);
    }

    private boolean isMessageReceived(AccessibilityNodeInfo nodeInfo) {
        final String[] sentMessageDesciption = {"Delivered", "Read", "Sent"};
        try{
            return Arrays.binarySearch(sentMessageDesciption, nodeInfo.getChild(nodeInfo.getChildCount() - 1).getContentDescription().toString()) == -1;
        } catch (Exception e) {
            return true;
        }
    }

    private String getMessageText(AccessibilityNodeInfo nodeInfo,  boolean isMessageReceived) {
        try{
            return (isMessageReceived ? nodeInfo.getChild(nodeInfo.getChildCount() - 2).getText().toString() : nodeInfo.getChild(nodeInfo.getChildCount() - 3).getText().toString());
        }catch (Exception e) {
            return "";
        }
    }

    private String getTimeText(AccessibilityNodeInfo nodeInfo,  boolean isMessageReceived) {
        try{
            return (isMessageReceived ? nodeInfo.getChild(nodeInfo.getChildCount() - 1).getText() : nodeInfo.getChild(nodeInfo.getChildCount() - 2).getText()).toString();
        }catch (Exception e) {
            return "";
        }
    }

    private void setDate(AccessibilityNodeInfo nodeInfo) {
        try{
            String dateString = nodeInfo.getChild(0).getText().toString();
            if (dateString.equals("TODAY")) {
                Calendar cal = Calendar.getInstance();
                int year  = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date  = cal.get(Calendar.DATE);
                cal.clear();
                cal.set(year, month, date);
                WhatsAppReadService.dateInCurrentScroll = cal.getTimeInMillis();
            } else if (dateString.equals("YESTERDAY")) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                int year  = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date  = cal.get(Calendar.DATE);
                cal.clear();
                cal.set(year, month, date);
                WhatsAppReadService.dateInCurrentScroll = cal.getTimeInMillis();
            }
            DateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            Date date = format.parse(dateString);
            WhatsAppReadService.dateInCurrentScroll = date.getTime();
        }catch (Exception e) {}
    }

    private Long getMessageTime(String timeString, Long date) {
        if (date == 0L)
            return 0L;

        try{
            int hrs = Integer.parseInt(timeString.substring(0, timeString.indexOf(':')));
            int mins = Integer.parseInt(timeString.substring(timeString.indexOf(':') + 1, timeString.indexOf(" ")));
            boolean isPm = timeString.substring(timeString.indexOf(" ") + 1).equalsIgnoreCase("pm");
            Long time =  date + (hrs * 3600000) + (mins * 60000);
            if (isPm) {
                time += 12 * 60 * 60 * 1000;
            }
            return time;
        } catch (Exception e) {
            return 0L;
        }
    }

    private String getWhatsAppChateeName(AccessibilityNodeInfoCompat conversationRoot) {
        try {
            return (String) conversationRoot.getChild(1).getChild(0).getText();
        } catch (Exception e) {
            return null;
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

    private void insertChat(LinkedList<Message> messages) {

        LinkedList<Message> savedChatLinkedList = chats.get(chateeName);
        if (savedChatLinkedList.size() == 0) {
            savedChatLinkedList.addAll(messages);
            return;
        }
        Message lastSavedMessage = savedChatLinkedList.getLast();
        boolean startInsert = false;
        Long dateFromLastMessage = lastSavedMessage.computeDate();
        for (Message message : messages) {
            if (startInsert || message.getUnread() || dateFromLastMessage <= message.getTime()) {
                if(message.getTime() == 0L) {
                    message.setTime(getMessageTime(message.getTimeText(), dateFromLastMessage));
                }
                savedChatLinkedList.addLast(message);
            }
            else if (message.hashCode() == lastSavedMessage.hashCode())
                startInsert = true;
        }

        if(chatee != null){
            List<MessageReadCheckpoint> messageCheckpoints = databaseHelper.getLocalMessageBreakPointsForChatee(chatee);


            MessageReadCheckpoint newMessageCheckpointForCurrentMessageWindow = new MessageReadCheckpoint();

            newMessageCheckpointForCurrentMessageWindow.setStartMessageHash(HashingService.computeHash(messages.getFirst()));
            newMessageCheckpointForCurrentMessageWindow.setStartMessageDate(messages.getFirst().getDate());
            newMessageCheckpointForCurrentMessageWindow.setEndMessageHash(HashingService.computeHash(messages.getLast()));
            newMessageCheckpointForCurrentMessageWindow.setEndMessageDate(messages.getLast().getDate());


            for(MessageReadCheckpoint checkpoint : messageCheckpoints){
                if(checkpoint.getStartMessageId() == null){ //checkpoint is start of list, ie, last read and saved checkpoint
                    if(checkpoint.getEndMessageDate().after(newMessageCheckpointForCurrentMessageWindow.getEndMessageDate())){
                        //ignore
                    }
                    else if(checkpoint.checkCheckpointOverlapAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //update current checkpoint's end message
                        //save non overlap messages
                    }
                    else if(checkpoint.checkCheckpointdisjointAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //save newly created checkpoint at this index, and save all messages
                    }
                }
                else{
                    MessageReadCheckpoint nextOlderCheckpoint = messageCheckpoints.get(messageCheckpoints.indexOf(checkpoint) + 1);

                    if(checkpoint.checkCheckpointdisjointAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //save newly created checkpoint at this index, and save all messages
                    }
                    else if(checkpoint.checkCheckpointOverlapAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //update current checkpoint's end message
                        //save non overlap messages
                    }
                    else if(checkpoint.checkCheckpointInBetweenCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //ignore all messages
                    }
                    else if(checkpoint.checkCheckpointOverlapBeforeCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //update current checkpoint's start message
                        //save non overlap messages

                        //also check for next older checkpoint in this list, and check if it combines ->
                        if(nextOlderCheckpoint.checkCheckpointdisjointAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                            //no overlap with next older checkpoint
                        }
                        else if(nextOlderCheckpoint.checkCheckpointOverlapAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                            //overlap with next older checkpoint, merge two checkpoints to form checkpoint, data gap complete
                        }

                    }
                    else if(checkpoint.checkCheckpointdisjointBeforeCheckpoint(newMessageCheckpointForCurrentMessageWindow)
                            && nextOlderCheckpoint.checkCheckpointdisjointAfterCheckpoint(newMessageCheckpointForCurrentMessageWindow)){
                        //save newly created checkpoint at this index, and save all messages only if after next checkpoint
                        //also check for next older checkpoint in this list, and check if it combines
                    }

                }
            }

            //TODO: write logic to combine checkpoints

//            for(Message message : messages){
//                for(MessageReadCheckpoint checkpoint : messageCheckpoints){
//                    if(checkpoint.getStartMessageId() == null){ //checkpoint is start of list, ie, last read and saved checkpoint
//
//                        if(message.getDate().after(checkpoint.getEndMessageDate())){
//                            //insert message
//
//                            //update/create checkpoint ->
//                            if(HashingService.checkHashMatches(message, checkpoint.getEndMessageHash())){
//                                //current checkpoint to be updated
//                            }
//                            else{
//                                //new checkpoint to be created -> use newMessageCheckpointForCurrentMessageWindow for the consecutive messages entry
//                            }
//                        }
//                    }
//                    else{
//                        if(checkpoint.checkMessageInBetweenCheckpoint(message)){
//                            break;
//                        }
//                        if(checkpoint.checkMessageBeforeCheckpoint(message)){
//                            continue;
//                        }
//
//                        if(checkpoint.checkMessageAfterCheckpoint(message)){
//                            //insert message
//
//                            //update/create checkpoint ->
//                            if(HashingService.checkHashMatches(message, checkpoint.getEndMessageHash())){
//                                //current checkpoint to be updated
//                            }
//                            else{
//                                //new checkpoint to be created -> use newMessageCheckpointForCurrentMessageWindow for the consecutive messages entry
//                            }
//                        }
//                    }
//                }
//            }
//
//            if(newMessageCheckpointForCurrentMessageWindow != null){
//                //insert message checkpoint in appropriate place
//            }
        }
        else{
            Log.i(TAG, "Chatee Number not found");
        }

        Log.d(TAG,  "SavedMessages -> " + savedChatLinkedList);
    }
}
