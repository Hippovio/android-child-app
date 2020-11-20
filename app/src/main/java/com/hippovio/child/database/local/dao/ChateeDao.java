package com.hippovio.child.database.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hippovio.child.database.local.entities.Chatee;

import java.util.List;

import io.reactivex.Single;

import static com.hippovio.child.constants.Sources.INSTAGRAM;
import static com.hippovio.child.constants.Sources.WHATSAPP;
import static com.hippovio.child.database.local.constants.TableName.CHATEE;

@Dao
public interface ChateeDao {

    @Query("SELECT * FROM " + CHATEE)
    List<Chatee> getAll();

    @Insert
    List<Long> insertAll(Chatee... chatees);

    @Insert
    Single<Long> insert(Chatee chatees);

    @Delete
    void delete(Chatee chatee);

    @Query("SELECT * FROM " + CHATEE + " WHERE identifier_value= :identifierValue limit 1")
    Single<Chatee> getWhatsappChateeForSender(String identifierValue);

}