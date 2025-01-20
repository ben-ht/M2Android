package com.example.projethembert.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.enums.MonsterType;
import com.example.projethembert.utils.Config;

import java.util.Random;

/**
 * Représente un monstre
 */
public class Monster implements Parcelable {
    public static final Creator<Monster> CREATOR = new Creator<Monster>() {
        @Override
        public Monster createFromParcel(Parcel source) {
            return new Monster(source);
        }

        @Override
        public Monster[] newArray(int size) {
            return new Monster[size];
        }
    };

    private static final Random random = new Random();

    /// Configuration de la partie, nécessaire en tant qu'attribut pour le fonctionnement du
    /// parcelable
    private final Config config;

    /// Puissance du monstre
    private final int power;

    /// Type du monstre
    private final MonsterType type;

    /// Niveau du monstre
    private int level;

    public Monster(int level, Config config) {
        this.config = config;
        type = randomType();
        power = (int) (Math.round(Math.random() * config.getDifficulty().getMaxMonsterPower()) + 1) * level;
    }

    protected Monster(Parcel source) {
        level = source.readInt();
        power = source.readInt();
        type = MonsterType.values()[source.readInt()];
        config = source.readParcelable(Config.class.getClassLoader());
    }

    /**
     * Retourne un type de monstre aléatoire
     * @return Type de monstre
     */
    private static MonsterType randomType() {
        int index = random.nextInt(MonsterType.class.getEnumConstants().length);
        return MonsterType.class.getEnumConstants()[index];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(power);
        dest.writeInt(type.ordinal());
        dest.writeParcelable(config, flags);
    }

    public int getPower() {
        return power;
    }

    public MonsterType getType() {
        return type;
    }
}
