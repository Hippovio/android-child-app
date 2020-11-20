package com.hippovio.child.database.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;
import com.hippovio.child.database.local.constants.TableName;
import com.hippovio.child.database.local.typeConverters.ChateeTypeConverter;
import com.hippovio.child.database.local.typeConverters.SourceConverter;
import com.hippovio.child.enums.ChateeTypes;
import com.hippovio.child.enums.Sources;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
 * Database entity for person whom the child is chatting with on current source
 * The same human chattee will be different chattee records for different sources
 */

@Getter
@Setter
@Entity(tableName = TableName.CHATEE)
public class Chatee extends com.hippovio.child.database.local.entities.Entity {

    @ColumnInfo(name = "source")
    @NonNull
    @TypeConverters({SourceConverter.class})
    private Sources chateeSource;

    @ColumnInfo(name = "type")
    @NonNull
    @TypeConverters({ChateeTypeConverter.class})
    private ChateeTypes chateeType;

    @ColumnInfo(name = "name")
    @NonNull
    private String chateeName;

    @ColumnInfo(name = "identifier_value")
    @NonNull
    private String identifierValue;

    @ColumnInfo(name = "identifier_field")
    @NonNull
    private String identifierField;

    @Ignore
    private String userId;

    @Builder(builderMethodName = "ChateeBuilder")
    public static Chatee chateeBuilder(@NonNull Sources chateeSource, @NonNull ChateeTypes chateeType,
                                       @NonNull String chateeName, @NonNull String identifierValue) {
        Chatee chatee = new Chatee();
        chatee.setChateeName(chateeName);
        chatee.setChateeSource(chateeSource);
        chatee.setChateeType(chateeType);
        chatee.setIdentifierValue(identifierValue);
        //TODO: Add indentifierField
        return chatee;
    }
}
