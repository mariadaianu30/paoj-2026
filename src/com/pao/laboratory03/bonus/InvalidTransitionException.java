package com.pao.laboratory03.bonus;

public class InvalidTransitionException extends RuntimeException {
    private Status fromStatus;
    private  Status toStatus;

    public InvalidTransitionException(Status from, Status to) {
        this.fromStatus = from;
        this.toStatus = to;
    }
    @Override
    public String getMessage() {
        return "Nu se poate trece din " + fromStatus + " in " + toStatus;
    }
}