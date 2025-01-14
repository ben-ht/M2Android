package com.example.projethembert.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.enums.FightResultEnum;

/**
 * Représente un résultat de combat
 */
public class FightResult implements Parcelable {
    /// Creator du Parcelable
    public static final Creator<FightResult> CREATOR = new Creator<FightResult>() {
        @Override
        public FightResult createFromParcel(Parcel source) {
            return new FightResult(source);
        }

        @Override
        public FightResult[] newArray(int size) {
            return new FightResult[size];
        }
    };

    /// Résultat du combat
    private final FightResultEnum result;

    /// Index de la salle dans laquelle s'est déroulé le combat
    private final int room;

    public FightResult(int room, FightResultEnum result) {
        this.result = result;
        this.room = room;
    }

    protected FightResult(Parcel source){
        room = source.readInt();
        result = FightResultEnum.values()[source.readInt()];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(room);
        dest.writeInt(result.ordinal());
    }

    public FightResultEnum getResult() {
        return result;
    }

    public int getRoom() {
        return room;
    }
}
