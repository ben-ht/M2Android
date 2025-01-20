package com.example.projethembert.entities.bonuses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Classe de création des bonus
 */
public class BonusFactory {
    private static final ArrayList<Class<? extends Bonus>> BONUS_TYPES = new ArrayList<>(Arrays.asList(
            PowerCharm.class,
            MagicPotion.class
    ));
    private static final Random RANDOM = new Random();

    /**
     * Créé un bonus aléatoire parmis la liste des bonus. Cette méthode ne peux pas retourner plus
     * d'un bonus du même type par partie.
     *
     * @return Bonus aléatoire
     */
    public static Bonus createBonus() {
        try {
            int index = RANDOM.nextInt(BONUS_TYPES.size());
            Class<? extends Bonus> bonus = BONUS_TYPES.remove(index);
            return bonus.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating bonus", e);
        }
    }

    /**
     * Réinitialise la liste des bonus, permettant à la méthode createBonus de recréer n'importe
     * quel bonus
     */
    public static void resetBonuses() {
        BONUS_TYPES.clear();
        BONUS_TYPES.addAll(Arrays.asList(PowerCharm.class, MagicPotion.class));
    }
}
