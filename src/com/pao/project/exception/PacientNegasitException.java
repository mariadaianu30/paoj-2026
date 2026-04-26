package com.pao.project.exception;

public class PacientNegasitException extends RuntimeException {
    public PacientNegasitException(String cnp) {
        super("Pacientul cu CNP " + cnp + " nu a fost gasit in sistem.");
    }
}

