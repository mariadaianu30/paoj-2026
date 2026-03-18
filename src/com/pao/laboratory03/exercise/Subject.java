package com.pao.laboratory03.exercise;

public enum Subject {
    PAOJ("Programare Avansată pe Obiecte", 6),
    BD("Baze de Date", 5),
    SO("Sisteme de Operare", 5),
    RC("Rețele de Calculatoare", 4);

    private final String fullName;
    private final int credits;

    private Subject(String fullName, int credits) {
        this.fullName = fullName;
        this.credits = credits;
    }

    /// getteri
    public String getFullName() {
        return fullName;
    }

    public int getCredits() {
        return credits;
    }

    /// - toString() → "PAOJ (Programare Avansată pe Obiecte, 6 credite)"
    ///suprascriu o metoda deja existenta
    @Override
    public String toString() {
        return name() + " (" + fullName + ", " + credits + " credite)";
    }
}