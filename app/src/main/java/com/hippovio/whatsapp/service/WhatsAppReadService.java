package com.hippovio.whatsapp.service;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.hippovio.screenread.AccessibilityHelper;
<<<<<<< HEAD
import com.hippovio.entities.Message;
=======
import com.hippovio.whatsapp.entities.Message;
>>>>>>> 6a3b23bd8dbb1cfdb34626760344f1e52e196f74
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class WhatsAppReadService {

    private static String window = null;
    private int lastToIndex = -1;
    private int lastFromIndex = -1;
    private int offset = 0;
    private String TAG = "WHATSAPP";
    private String editTextViewId = "com.whatsapp:id/entry";
    private static Map<String, LinkedList<Message>> chats = new HashMap<>();

    private static Long dateInCurrentScroll = 0L;
    private boolean areUnreadMessages = false;

    public void whatsAppEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfoCompat rootInActiveWindow) {

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (window == null)
                return;
            readChat(accessibilityEvent);

        } else if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfoCompat rootNode = AccessibilityHelper.getNodeInfo(rootInActiveWindow, editTextViewId);
            if (rootNode == null) {
                window = null;
                lastToIndex = -1;
                lastFromIndex = -1;
                offset = 0;
                return;
            }
            window = getWhatsAppChatLabel(rootInActiveWindow);
            if (!chats.containsKey(window)) {
                chats.put(window, new LinkedList<Message>());
            }
            areUnreadMessages = false;
        } else
            return;
    }

    private void readChat(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo source = accessibilityEvent.getSource();
        int toIndex = accessibilityEvent.getToIndex(), fromIndex = accessibilityEvent.getFromIndex();

        LinkedList<Message> messages = readMessages(source);

        insertChat(messages);

        if (lastToIndex == -1) {
            lastToIndex = accessibilityEvent.getToIndex();
            lastFromIndex = accessibilityEvent.getFromIndex();

        } else if (lastFromIndex > fromIndex || lastToIndex > toIndex){
            Log.d(TAG, "Scroll Down");
            if ((toIndex - fromIndex + 1) == source.getChildCount()) {
                Log.d(TAG, "New Messages Added: " + (lastFromIndex - fromIndex));
            }
        } else if (fromIndex - lastFromIndex > 70 || toIndex - lastToIndex > 70){
            Log.d(TAG, "Scroll Down");
            offset -= 100;
        } else {
            Log.d(TAG, "Scroll Up");
            if ((toIndex - fromIndex + 1) == source.getChildCount()) {
                Log.d(TAG, "New Messages Added: " + (toIndex - lastToIndex));
            }
        }

        lastToIndex = toIndex;
        lastFromIndex = fromIndex;
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

        return new Message(window, msgText,isMessageReceived, messageTime, timeText, areUnreadMessages);
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

    private String getWhatsAppChatLabel(AccessibilityNodeInfoCompat conversationRoot) {
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
        LinkedList<Message> savedChatLinkedList = chats.get(window);
        if (savedChatLinkedList.size() == 0) {
            savedChatLinkedList.addAll(messages);
            return;
        }
        Message lastSavedMessage = savedChatLinkedList.getLast();
        boolean startInsert = false;
        Long dateFromLastMessage = lastSavedMessage.getDate();
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

        Log.d(TAG,  "SavedMessages -> " + savedChatLinkedList);
    }
}
