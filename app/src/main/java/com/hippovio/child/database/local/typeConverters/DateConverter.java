package com.hippovio.child.database.local.typeConverters;

import androidx.room.TypeConverter;

import com.hippovio.child.constants.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConverter {

    static final DateFormat df = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return df.format(date);
    }
}
