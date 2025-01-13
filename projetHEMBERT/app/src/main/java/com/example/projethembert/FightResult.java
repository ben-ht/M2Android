package com.example.projethembert;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FightResult implements Parcelable {
    public enum FightResultEnum {
        WON,
        LOST,
        FLED
    }

    public final int room;
    public final FightResultEnum result;
    public final String message;

    public FightResult(int room, FightResultEnum result) {
        this.result = result;
        this.room = room;
        switch (this.result) {
            case WON:
                this.message = "Vous avez remporté le combat";
                break;
            case LOST:
                this.message = "Vous avez perdu le combat";
                break;
            case FLED:
                this.message = "Vous avez fui";
                break;
            default:
                this.message = "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FightResult(Parcel source){
        room = source.readInt();
        result = FightResultEnum.values()[source.readInt()];
        message = source.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(room);
        dest.writeInt(result.ordinal());
        dest.writeString(message);
    }

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
}
