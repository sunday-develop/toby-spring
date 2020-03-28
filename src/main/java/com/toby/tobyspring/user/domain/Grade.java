package com.toby.tobyspring.user.domain;

public enum Grade {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Grade next;

    Grade(int value, Grade next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return value;
    }

    public Grade nextGrade() {
        return this.next;
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
