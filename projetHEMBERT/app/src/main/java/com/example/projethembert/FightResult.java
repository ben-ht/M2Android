package com.example.projethembert;

import java.io.Serializable;

public class FightResult implements Serializable {
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
}
