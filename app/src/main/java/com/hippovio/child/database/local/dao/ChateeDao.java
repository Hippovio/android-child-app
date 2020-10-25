package com.hippovio.child.database.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hippovio.child.database.local.entities.Chatee;

import java.util.List;

import static com.hippovio.child.database.local.constants.Sources.INSTAGRAM;
import static com.hippovio.child.database.local.constants.Sources.WHATSAPP;
import static com.hippovio.child.database.local.constants.TableName.CHATEE;

@Dao
public interface ChateeDao {

    @Query("SELECT * FROM " + CHATEE)
    List<Chatee> getAll();

    @Insert
    void insertAll(Chatee... chatees);

    @Delete
    void delete(Chatee chatee);

    @Query("SELECT * FROM " + CHATEE + " WHERE source= '" + WHATSAPP + "' AND identifier= :phoneNumber limit 1")
    Chatee getWhatsappChateeForSender(String phoneNumber);

    @Query("SELECT * FROM " + CHATEE + " WHERE source= '" + INSTAGRAM + "' AND identifier= :instagramUsername")
    List<Chatee> getInstagramChateeForSender(String instagramUsername);
}