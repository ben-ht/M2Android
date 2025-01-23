package com.example.projethembert.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.projethembert.entities.LeaderboardEntry;
import com.example.projethembert.repository.utils.DateConverter;

/**
 * Classe de la base de données de l'application.
 * La base de donnée est un singleton.
 */
@androidx.room.Database(entities = {LeaderboardEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class Database extends RoomDatabase {

    /// Instance de la base de données
    private static Database instance;

    /// Retourne l'instance de la base de données si elle existe, sinon la créée
    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "leaderboard_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    /// Table des scores
    public abstract LeaderboardRepository leaderboardRepository();

}
