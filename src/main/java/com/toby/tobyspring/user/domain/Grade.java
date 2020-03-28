package com.toby.tobyspring.user.domain;

public enum Grade {
    BASIC(1), SILVER(2), GOLD(3);

    private final int value;

    Grade(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static Grade valueOf(int value) {
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
