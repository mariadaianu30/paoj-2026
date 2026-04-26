package com.pao.project.model;

import java.util.*;

public class FisaMedicala {
    private final Pacient pacient;
    private final List<Consultatie> consultatii;
    private final Map<String, String> analize;

    public FisaMedicala(Pacient pacient) {
        this.pacient = pacient;
        this.consultatii = new ArrayList<>();
        this.analize = new HashMap<>();
    }

    public void adaugaConsultatie(Consultatie c) {
        if (c == null) throw new IllegalArgumentException("Consultatia nu poate fi null.");
        consultatii.add(c);
    }

    public List<Consultatie> getConsultatii() { return Collections.unmodifiableList(consultatii); }

    public void adaugaAnaliza(String nume, String rezultat) {
        if (nume == null || nume.isBlank()) throw new IllegalArgumentException("Numele analizei nu poate fi gol.");
        if (rezultat == null || rezultat.isBlank()) throw new IllegalArgumentException("Rezultatul analizei nu poate fi gol.");
        analize.put(nume, rezultat);
    }

    public String getAnaliza(String nume) {
        if (!analize.containsKey(nume)) throw new IllegalArgumentException("Analiza negasita: " + nume);
        return analize.get(nume);
    }

    public int getNumarConsultatii() { return consultatii.size(); }

    public Consultatie getUltimaConsultatie() {
        return consultatii.isEmpty() ? null : consultatii.get(consultatii.size() - 1);
    }

    public void afiseaza() {
        System.out.println(toString());
        System.out.println("  Consultatii:");
        for (Consultatie c : consultatii) {
            System.out.print("    ");
            c.afiseaza();
        }
        System.out.println("  Analize:");
        for (Map.Entry<String, String> entry : analize.entrySet()) {
            System.out.println("    " + entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public String toString() {
        return "FisaMedicala{consultatii=" + consultatii.size() + ", analize=" + analize.size() + "}";
    }
}