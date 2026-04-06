package com.pao.laboratory07.exercise1.exceptions;

public class CannotCancelFinalOrderException extends Exception {
    public CannotCancelFinalOrderException() {
        super();
    }

    public CannotCancelFinalOrderException(String message) {
        super(message);
    }
}