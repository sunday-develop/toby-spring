package com.pplenty.studytoby;

/**
 * Created by yusik on 2020/03/28.
 */
public enum Level {

    BASIC(1, Level.SILVER),
    SILVER(2, Level.GOLD),
    GOLD(3, null),
    ;


    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return value;
    }

    public Level nextLevel() {
        return next;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1:
                return BASIC;
            case 2:
                return SILVER;
            case 3:
                return GOLD;
            default:
                throw new AssertionError("Unknown value: " + value);
        }
    }
}
