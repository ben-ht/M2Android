package com.example.projethembert.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.projethembert.entities.LeaderboardEntry;

import java.util.List;

/**
 * Interface de communication avec la table des scores de la base de données
 */
@Dao
public interface LeaderboardRepository {

    /// Insère un LeaderboardEntry en bdd
    @Insert
    void insert(LeaderboardEntry entry);

    /**
     * Récupère les 10 meilleurs scores selon le niveau de difficulté
     * @param difficulty Niveau de difficulté dans lequel chercher les scores
     */
    @Query("Select * FROM leaderboard WHERE difficulty = :difficulty ORDER by levelReached DESC LIMIT 10")
    List<LeaderboardEntry> getBestScores(String difficulty);

    /**
     * Supprime les scores qui sont au delà du top 10 pour ne garder que les 10 meilleurs en bdd
     * @param difficulty
     */
    @Query("DELETE FROM leaderboard WHERE id NOT IN (SELECT id FROM leaderboard WHERE difficulty = :difficulty ORDER BY levelReached DESC LIMIT 10)")
    void deleteExcessScores(String difficulty);

    /**
     * Insère un score en bdd seulement s'il fait partie du top 10
     * @param entry Score
     */
    @Transaction
    default void insertIfTop10(LeaderboardEntry entry) {
        List<LeaderboardEntry> currentTop10 = getBestScores(entry.getDifficulty());

        boolean isTop10 = currentTop10.size() < 10
                || entry.getLevelReached() > currentTop10.get(9).getLevelReached()
                || (entry.getLevelReached() == currentTop10.get(9).getLevelReached()
                && entry.getPower() > currentTop10.get(9).getPower());

        if (isTop10) {
            insert(entry);
            deleteExcessScores(entry.getDifficulty());
        }
    }
}
