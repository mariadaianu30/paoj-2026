package com.pao.laboratory09.exercise2;

public enum Status {
    PENDING(0),
    PROCESSED(1),
    REJECTED(2);

    public final int code;

    Status(int code) {
        this.code = code;
    }

    public static Status fromCode(int c) {
        return switch (c) {
            case 1 -> PROCESSED;
            case 2 -> REJECTED;
            default -> PENDING;
        };
    }

    public static Status fromString(String s) {
        return switch (s) {
            case "PROCESSED" -> PROCESSED;
            case "REJECTED" -> REJECTED;
            default -> PENDING;
        };
    }
}