package com.pao.project.service;

import com.pao.project.exception.ProgramareConflictException;
import com.pao.project.model.Programare;

import java.util.*;

public class ProgramareService {
    private static ProgramareService instance;
    private final TreeSet<Programare> programari = new TreeSet<>();

    private ProgramareService() {}

    public static ProgramareService getInstance() {
        if (instance == null) instance = new ProgramareService();
        return instance;
    }

    public void adaugaProgramare(Programare p) throws ProgramareConflictException {
        if (p.getMedic().areConflictOrar(p)) {
            throw new ProgramareConflictException("Medicul " + p.getMedic().getNumeComplet()
                    + " are deja o programare la " + p.getDataOra());
        }
        programari.add(p);
        p.getMedic().adaugaProgramare(p);
    }

    public void anuleazaProgramare(String id) {
        Programare gasita = null;
        for (Programare p : programari) {
            if (p.getIdProgramare().equals(id)) {
                gasita = p;
                break;
            }
        }
        if (gasita == null) throw new IllegalArgumentException("Programare negasita: " + id);
        gasita.anuleaza();
        gasita.getMedic().anuleazaProgramare(id);
    }

    public List<Programare> getProgramariMedic(String parafa) {
        List<Programare> result = new ArrayList<>();
        for (Programare p : programari) {
            if (p.getMedic().getParafa().equals(parafa)) result.add(p);
        }
        return result;
    }

    public List<Programare> getProgramariPacient(String cnp) {
        List<Programare> result = new ArrayList<>();
        for (Programare p : programari) {
            if (p.getPacient().getCNP().equals(cnp)) result.add(p);
        }
        return result;
    }

    public List<Programare> getProgramariActive() {
        List<Programare> result = new ArrayList<>();
        for (Programare p : programari) {
            if (p.esteActiva()) result.add(p);
        }
        return result;
    }

    public TreeSet<Programare> listeazaToate() { return new TreeSet<>(programari); }
}