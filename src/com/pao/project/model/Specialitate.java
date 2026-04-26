package com.pao.project.model;

public enum Specialitate {
    MEDICINA_GENERALA("Medicina generala"),
    CARDIOLOGIE("Cardiologie"),
    PEDIATRIE("Pediatrie"),
    DERMATOLOGIE("Dermatologie"),
    NEUROLOGIE("Neurologie"),
    ORTOPEDIE("Ortopedie");

    private final String denumire;

    Specialitate(String denumire) { this.denumire = denumire; }

    public String getDenumire() { return denumire; }
}