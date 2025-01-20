package com.example.projethembert.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.bonuses.Bonus;
import com.example.projethembert.utils.Config;

/**
 * Représente une salle
 */
public class Room implements Parcelable {
    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
    /// Configuration de la partie
    private final Config config;

    /// Id de la salle
    private final int id;

    /// Monstre présent dans la salle
    private final Monster monster;

    /// Bonus de la salle, peut être null car toutes les salles n'ont pas de bonus
    private Bonus bonus;

    public Room(int id, int level, Config config) {
        this.config = config;
        monster = new Monster(level, config);
        this.id = id;
    }

    protected Room(Parcel in) {
        id = in.readInt();
        monster = in.readParcelable(Monster.class.getClassLoader());
        bonus = in.readParcelable(Bonus.class.getClassLoader());
        config = in.readParcelable(Config.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(monster, flags);
        dest.writeParcelable(bonus, flags);
        dest.writeParcelable(config, flags);
    }

    /**
     * Indique si la salle contient un bonus
     * @return True si la salle contient un bonus, false si elle n'en contient pas ou s'il a été
     * utilisé
     */
    public boolean hasNoBonus() {
        return bonus == null;
    }

    /**
     * Utilise le bonus
     */
    public void useBonus(Player player) {
        bonus.use(player);
        bonus = null;
    }

    public Monster getMonster() {
        return monster;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public int getId() {
        return id;
    }
}
