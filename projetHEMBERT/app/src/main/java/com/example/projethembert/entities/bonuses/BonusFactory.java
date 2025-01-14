package com.example.projethembert.entities.bonuses;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BonusFactory {
    private static final List<Class<? extends Bonus>> BONUS_TYPES = Arrays.asList(
            PowerCharm.class,
            MagicPotion.class);
    private static final Random RANDOM = new Random();

    public static Bonus createBonus() {
        try {
            int index = RANDOM.nextInt(BONUS_TYPES.size());
            return BONUS_TYPES.get(index).getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException("Error creating bonus", e);
        }
    }
}
