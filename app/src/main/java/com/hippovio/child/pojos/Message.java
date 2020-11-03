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
    private String timeText;
    private Boolean isUnread;
    private Date dateTime = null;
    private String messageHash;
    //state - enum: NEW, UNREAD, UPLOADED, COMPLETE, READ
    //source - enum: WHATSAPP, FB
    // Global:
    // childId
    @Builder(builderMethodName = "MessageBuilder")
    public static Message messageBuilder(Chatee chatee, String msg, Boolean isReceived, Date date, String timeText, boolean isUnread) {
        Message message = new Message();
        message.setChatee(chatee);
        message.setMsg(msg);
        message.setIsReceived(isReceived);
        message.setTimeText(timeText);
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
                ", timeText='" + timeText + "\'\n" +
                ", isUnread='" + isUnread + "\'\n" +
                ", messageHash=" + messageHash + '\n' +
                '}';
    }

    public void setDate(Date date) {
        if (date == null)
            return;
        try{
            int hrs = Integer.parseInt(timeText.substring(0, timeText.indexOf(':')));
            int mins = Integer.parseInt(timeText.substring(timeText.indexOf(':') + 1, timeText.indexOf(" ")));
            boolean isPm = timeText.substring(timeText.indexOf(" ") + 1).equalsIgnoreCase("pm");
            Long time =  date.getTime() + (hrs * 3600000) + (mins * 60000);
            if (isPm) {
                time += 12 * 60 * 60 * 1000;
            }
            dateTime = new Date(time);
        } catch (Exception e) {}
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
