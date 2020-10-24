package com.hippovio.child.database.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hippovio.child.database.local.constants.TableName;

@Entity(tableName = TableName.CHATEE)
public class Chatee {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private String chateeId;

    @ColumnInfo(name = "source")
    @NonNull
    private String chateeSource;

    @ColumnInfo(name = "type")
    @NonNull
    private String chateeType;

    @ColumnInfo(name = "name")
    @NonNull
    private String chateeName;

    @ColumnInfo(name = "identifier")
    @NonNull
    private String chateeIdentifier;

    @NonNull
    public String getChateeId() {
        return chateeId;
    }

    public void setChateeId(@NonNull String chateeId) {
        this.chateeId = chateeId;
    }

    @NonNull
    public String getChateeSource() {
        return chateeSource;
    }

    public void setChateeSource(@NonNull String chateeSource) {
        this.chateeSource = chateeSource;
    }

    @NonNull
    public String getChateeType() {
        return chateeType;
    }

    public void setChateeType(@NonNull String chateeType) {
        this.chateeType = chateeType;
    }

    @NonNull
    public String getChateeName() {
        return chateeName;
    }

    public void setChateeName(@NonNull String chateeName) {
        this.chateeName = chateeName;
    }

    @NonNull
    public String getChateeIdentifier() {
        return chateeIdentifier;
    }

    public void setChateeIdentifier(@NonNull String chateeIdentifier) {
        this.chateeIdentifier = chateeIdentifier;
    }
}
