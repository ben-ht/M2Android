package com.example.projethembert.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.projethembert.entities.LeaderboardEntry;
import com.example.projethembert.repository.utils.DateConverter;

@androidx.room.Database(entities = {LeaderboardEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract LeaderboardRepository leaderboardRepository();

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "leaderboard_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
