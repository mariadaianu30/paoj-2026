package com.pao.project.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Consultatie {
    private final Medic medic;
    private final Pacient pacient;
    private final LocalDateTime dataOra;
    private final String simptome;
    private String diagnostic;
    private Reteta reteta;

    public Consultatie(Medic medic, Pacient pacient, String simptome) {
        this.medic = medic;
        this.pacient = pacient;
        this.simptome = simptome;
        this.dataOra = LocalDateTime.now();
    }

    public void setDiagnostic(String diagnostic) {
        if (diagnostic == null || diagnostic.isBlank()) throw new IllegalArgumentException("Diagnosticul nu poate fi gol.");
        this.diagnostic = diagnostic;
    }

    public void setReteta(Reteta reteta) {
        if (reteta == null) throw new IllegalArgumentException("Reteta nu poate fi null.");
        this.reteta = reteta;
    }

    public boolean areReteta() { return reteta != null; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consultatie other)) return false;
        return Objects.equals(medic, other.medic)
                && Objects.equals(pacient, other.pacient)
                && Objects.equals(dataOra, other.dataOra);
    }

    @Override
    public int hashCode() { return Objects.hash(medic, pacient, dataOra); }

    @Override
    public String toString() {
        String data = dataOra.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return "Consultatie{medic='" + medic.getNumeComplet()
                + "', pacient='" + pacient.getNumeComplet()
                + "', data='" + data
                + "', diagnostic='" + diagnostic + "'}";
    }

    public void afiseaza() {
        System.out.println(toString());
        if (areReteta()) reteta.afiseaza();
    }

    public Medic getMedic() { return medic; }
    public Pacient getPacient() { return pacient; }
    public LocalDateTime getDataOra() { return dataOra; }
    public String getSimptome() { return simptome; }
    public String getDiagnostic() { return diagnostic; }
    public Reteta getReteta() { return reteta; }
}