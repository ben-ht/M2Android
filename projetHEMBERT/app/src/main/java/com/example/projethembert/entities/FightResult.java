package com.example.projethembert.entities;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.enums.FightResultEnum;

/**
 * Représente un résultat de combat
 */
public class FightResult implements Parcelable {
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
    /// Indique si un bonus à été utilisé lors du combat
    private final boolean bonusUsed;
    /// Résultat du combat
    private final FightResultEnum result;
    /// Index de la salle dans laquelle s'est déroulé le combat
    private final int roomId;

    public FightResult(int roomId, FightResultEnum result, boolean bonusUsed) {
        this.result = result;
        this.roomId = roomId;
        this.bonusUsed = bonusUsed;
    }

    protected FightResult(Parcel source) {
        roomId = source.readInt();
        result = FightResultEnum.values()[source.readInt()];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            bonusUsed = source.readBoolean();
        } else {
            bonusUsed = source.readByte() != 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(roomId);
        dest.writeInt(result.ordinal());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(bonusUsed);
        } else {
            dest.writeByte((byte) (bonusUsed ? 1 : 0));
        }
    }

    public FightResultEnum getResult() {
        return result;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isBonusUsed() {
        return bonusUsed;
    }
}
