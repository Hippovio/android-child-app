package com.hippovio.child.services.messageRead.helpers;

import android.util.Pair;

import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.pojos.Message;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class MessageHelper {

    /**
     * Finds the overlap of messages frame in the checkpoint
     * @param checkpoint {@link MessageReadCheckpoint} Un-read message window
     * @param messages {@link Message} List of messages
     * @return the index of overlap first: startIndex, second: endIndex
     */
    public static Pair<Integer, Integer> findMessageOverlap(MessageReadCheckpoint checkpoint, List<Message> messages) {
        int startIndex = -1, lastIndex = -1;
        ListIterator<Message> it = messages.listIterator();
        while (it.hasNext()) {
            Message message = it.next();
            if(message.getMessageHash().equals(checkpoint.getStartMessageHash()))
                startIndex = it.previousIndex();
            else if (message.getMessageHash().equals(checkpoint.getEndMessageHash()))
                lastIndex = it.previousIndex();
        }
        return new Pair<>(startIndex, lastIndex);
    }

    /**
     * Updates the message list with the date
     * @param messages
     * @param date
     * @return updated list of messages.
     */
    public static List<Message> updateDate(List<Message> messages, Date date) {
        messages.forEach(message -> {
            if (message.getDateTime() == null)
                message.setDate(date);
        });
        return messages;
    }
}
