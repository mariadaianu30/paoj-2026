package com.pao.project.exception;

public class MedicNedisponibilException extends RuntimeException {
    public MedicNedisponibilException(String numeComplet) {
        super("Medicul " + numeComplet + " nu este disponibil in acest moment.");
    }
}