package com.example.projethembert.entities.bonuses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BonusFactory {
    private static final ArrayList<Class<? extends Bonus>> BONUS_TYPES = new ArrayList<>(Arrays.asList(
            PowerCharm.class,
            MagicPotion.class
    ));
    private static final Random RANDOM = new Random();

    public static Bonus createBonus() {
        try {
            int index = RANDOM.nextInt(BONUS_TYPES.size());
            Class<? extends Bonus> bonus = BONUS_TYPES.remove(index);
            return bonus.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException("Error creating bonus", e);
        }
    }

    public static void resetBonuses() {
        BONUS_TYPES.clear();
        BONUS_TYPES.addAll(Arrays.asList(PowerCharm.class, MagicPotion.class));
    }
}
