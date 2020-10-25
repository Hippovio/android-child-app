package com.hippovio.child.pojos;

import com.hippovio.child.database.local.entities.Chatee;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Message {
    private String id;
    private Chatee chatee;
    private String msg;
    private Boolean isReceived;
    private Long time;
    private String timeText;
    private Boolean isUnread;
    private Date date;
    //state - enum: NEW, UNREAD, UPLOADED, COMPLETE, READ
    //source - enum: WHATSAPP, FB
    // Global:
    // childId

    public Message(Chatee chatee, String msg, Boolean isReceived, Long time, String timeText, boolean isUnread) {
        this.chatee = chatee;
        this.msg = msg;
        this.isReceived = isReceived;
        this.time = time;
        this.timeText = timeText;
        this.isUnread = isUnread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chatee getChatee() {
        return chatee;
    }

    public void setChatee(Chatee chatee) {
        this.chatee = chatee;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getReceived() {
        return isReceived;
    }

    public void setReceived(Boolean received) {
        isReceived = received;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public Boolean getUnread() {
        return isUnread;
    }

    public void setUnread(Boolean unread) {
        isUnread = unread;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(chatee, message.chatee) &&
                Objects.equals(msg, message.msg) &&
                Objects.equals(isReceived, message.isReceived) &&
                Objects.equals(time, message.time);
    }

    @Override
    public String toString() {
        return "Message{\n" +
                "chatee='" + chatee.toString() + "\'\n" +
                ", msg='" + msg + "\'\n" +
                ", isReceived=" + isReceived + "\n" +
                ", time=" + time + '\n' +
                ", timeText='" + timeText + "\'\n" +
                ", isUnread='" + isUnread + "\'\n" +
                ", hashcode=" + this.hashCode() + '\n' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(msg, isReceived, timeText);
    }

    public Long computeDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.time);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTimeInMillis();
    }
}
