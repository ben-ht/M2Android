package com.example.projethembert;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Random;

public class Opponent implements Parcelable {
    private final int power;
    private final MonsterType type;
    private static final Random random = new Random();

    public Opponent(){
        type = randomType();
        power = (int) Math.round(Math.random() * 150) + 1;
    }

    private static MonsterType randomType(){
        int index = random.nextInt(MonsterType.class.getEnumConstants().length);
        return MonsterType.class.getEnumConstants()[index];
    }

    public int getPower(){
        return power;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(power);
        dest.writeInt(type.ordinal());
    }

    protected Opponent(Parcel source){
        power = source.readInt();
        type = MonsterType.values()[source.readInt()];
    }

    public static final Creator<Opponent> CREATOR = new Creator<Opponent>() {
        @Override
        public Opponent createFromParcel(Parcel source) {
            return new Opponent(source);
        }

        @Override
        public Opponent[] newArray(int size) {
            return new Opponent[size];
        }
    };
}
