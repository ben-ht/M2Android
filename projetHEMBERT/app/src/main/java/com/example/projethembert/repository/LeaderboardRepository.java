package com.example.projethembert.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.projethembert.entities.LeaderboardEntry;

import java.util.List;

@Dao
public interface LeaderboardRepository {
    @Insert
    void insert(LeaderboardEntry entry);

    @Query("Select * FROM leaderboard WHERE difficulty = :difficulty ORDER by levelReached DESC LIMIT 10")
    List<LeaderboardEntry> getBestScores(String difficulty);

    @Query("DELETE FROM leaderboard WHERE id NOT IN (SELECT id FROM leaderboard WHERE difficulty = :difficulty ORDER BY levelReached DESC LIMIT 10)")
    void deleteExcessScores(String difficulty);

    @Transaction
    default void insertIfTop10(LeaderboardEntry entry) {
        List<LeaderboardEntry> currentTop10 = getBestScores(entry.getDifficulty());

        boolean isTop10 = currentTop10.size() < 10 || entry.getLevelReached() > currentTop10.get(9).getLevelReached();

        if (isTop10) {
            insert(entry);
            deleteExcessScores(entry.getDifficulty());
        }
    }
}
