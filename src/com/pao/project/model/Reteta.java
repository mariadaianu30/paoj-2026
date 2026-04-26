package com.pao.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Reteta {
    private final String idReteta;
    private final Medic medic;
    private final Pacient pacient;
    private final LocalDate dataEmiterii;
    private final LocalDate dataExpirarii;
    private final List<String> medicamente;

    public Reteta(Medic medic, Pacient pacient, String[] medicamente) {
        this.idReteta = "RET-" + System.currentTimeMillis();
        this.medic = medic;
        this.pacient = pacient;
        this.dataEmiterii = LocalDate.now();
        this.dataExpirarii = dataEmiterii.plusDays(30);
        this.medicamente = new ArrayList<>(Arrays.asList(medicamente));
    }

    public void adaugaMedicament(String m) {
        if (m == null || m.isBlank()) throw new IllegalArgumentException("Medicamentul nu poate fi gol.");
        medicamente.add(m);
    }

    public List<String> getMedicamente() { return Collections.unmodifiableList(medicamente); }

    public boolean esteValabila() { return LocalDate.now().isBefore(dataExpirarii); }

    public long getZileValabilitate() {
        long zile = ChronoUnit.DAYS.between(LocalDate.now(), dataExpirarii);
        return zile < 0 ? 0 : zile;
    }

    @Override
    public String toString() {
        return "Reteta{id='" + idReteta + "', medic='" + medic.getNumeComplet()
                + "', medicamente=" + medicamente + ", valabila=" + esteValabila() + "}";
    }

    public void afiseaza() {
        System.out.println(toString());
        System.out.println("  Medicamente:");
        for (String m : medicamente) System.out.println("    - " + m);
        System.out.println("  Zile valabilitate ramase: " + getZileValabilitate());
    }

    public String getIdReteta() { return idReteta; }
    public Medic getMedic() { return medic; }
    public Pacient getPacient() { return pacient; }
    public LocalDate getDataEmiterii() { return dataEmiterii; }
    public LocalDate getDataExpirarii() { return dataExpirarii; }
}