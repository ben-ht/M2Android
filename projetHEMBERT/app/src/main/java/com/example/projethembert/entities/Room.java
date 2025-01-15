package com.example.projethembert.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.bonuses.Bonus;

/**
 * Représente une salle
 */
public class Room implements Parcelable {
    /// Monstre
    private final Monster monster;

    /// Id de la salle
    private final int id;

    private Bonus bonus;

    public Room(int id, int level){
        monster = new Monster(level);
        this.id = id;
    }

    protected Room(Parcel in) {
        id = in.readInt();
        monster = in.readParcelable(Monster.class.getClassLoader());
        bonus = in.readParcelable(Bonus.class.getClassLoader());
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
    }

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

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public Monster getMonster() {
        return monster;
    }

    public int getId() {
        return id;
    }
}
