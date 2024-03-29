package com.hippovio.child.pojos;

import com.hippovio.child.database.local.entities.Chatee;
import com.hippovio.child.hashing.HashingService;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String id;
    private Chatee chatee;
    private String msg;
    private Boolean isReceived;
    private Long timeInDayMillies;
    private Boolean isUnread;
    private Date dateTime = null;
    private String messageHash;

    @Builder(builderMethodName = "MessageBuilder")
    public static Message messageBuilder(Chatee chatee, String msg, Boolean isReceived, Date date, Long timeInDayMillies, boolean isUnread) {
        Message message = new Message();
        message.setChatee(chatee);
        message.setMsg(msg);
        message.setIsReceived(isReceived);
        message.setTimeInDayMillies(timeInDayMillies);
        message.setDate(date);
        message.setIsUnread(isUnread);
        message.setMessageHash(HashingService.computeHash(message));
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(chatee, message.chatee) &&
                Objects.equals(msg, message.msg) &&
                Objects.equals(isReceived, message.isReceived) &&
                Objects.equals(dateTime, message.dateTime);
    }

    @Override
    public String toString() {
        return "Message{\n" +
                "id='" + id + "\'\n" +
                "chatee='" + chatee + "\'\n" +
                ", msg='" + msg + "\'\n" +
                ", isReceived=" + isReceived + "\n" +
                ", dateTime=" + dateTime + '\n' +
                ", timeInDayMillies='" + timeInDayMillies + "\'\n" +
                ", isUnread='" + isUnread + "\'\n" +
                ", messageHash=" + messageHash + '\n' +
                '}';
    }

    public void setDate(Date date) {
        if (date == null)
            return;
        dateTime = new Date(date.getTime() + timeInDayMillies);
    }

    public Date getDate() {
        if (dateTime == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        return c.getTime();
    }
}
