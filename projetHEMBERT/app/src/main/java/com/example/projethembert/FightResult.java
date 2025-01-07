package com.example.projethembert;

import java.io.Serializable;

public class FightResult implements Serializable {
    public enum FightResultEnum {
        WON,
        LOST,
        FLED
    }

    public final FightResultEnum result;
    public final String message;

    public FightResult(FightResultEnum result, String message) {
        this.result = result;
        this.message = message;
    }
}
