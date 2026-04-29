package com.pao.laboratory08.exercise1.exceptions;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String nume) {
        super("Studentul cu numele '" + nume + "' nu a fost gasit.");
    }
}