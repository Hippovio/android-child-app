package com.hippovio.databaseHelper.typeConverters;

import androidx.room.TypeConverter;

import com.hippovio.entities.enums.PackageName;

public class PackageNameConverter {

    @TypeConverter
    public static PackageName toPackageName(int value) {
        for(PackageName packageName : PackageName.values()) {
            if (packageName.ordinal() == value)
                return packageName;
        }
        throw new IllegalArgumentException("Could not recognize PackageName value: " + value);
    }

    @TypeConverter
    public static Integer toInteger(PackageName packageName) {
        return packageName.ordinal();
    }
}
