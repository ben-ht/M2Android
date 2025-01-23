package com.example.projethembert.repository.utils;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Classe de convertion entre java.util.Date et une date SQLite
 */
public class DateConverter {

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date fromTimeStamp(Long value) {
        return value == null ? null : new Date(value);
    }
}
