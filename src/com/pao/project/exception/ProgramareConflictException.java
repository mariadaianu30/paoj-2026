package com.pao.project.exception;

public class ProgramareConflictException extends Exception {
    public ProgramareConflictException(String detalii) {
        super("Conflict de programare: " + detalii);
    }
}
