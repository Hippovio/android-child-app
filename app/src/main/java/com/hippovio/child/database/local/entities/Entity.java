package com.hippovio.child.database.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.hippovio.child.database.local.typeConverters.DateConverter;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Entity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private Long id;

    @NonNull
    @ColumnInfo(name = "createdDate")
    @TypeConverters({DateConverter.class})
    private Date createdDate;

    @NonNull
    @ColumnInfo(name = "updatedDate")
    @TypeConverters({DateConverter.class})
    private Date updatedDate;
}
