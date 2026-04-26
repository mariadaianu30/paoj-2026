package com.pao.project.service;

import com.pao.project.exception.PacientNegasitException;
import com.pao.project.model.Pacient;

import java.util.*;

public class PacientService {
    private static PacientService instance;
    private final Map<String, Pacient> pacienti = new HashMap<>();

    private PacientService() {}

    public static PacientService getInstance() {
        if (instance == null) instance = new PacientService();
        return instance;
    }

    public void adaugaPacient(Pacient p) {
        if (p == null) throw new IllegalArgumentException("Pacientul nu poate fi null.");
        if (pacienti.containsKey(p.getCNP()))
            throw new IllegalArgumentException("Pacient deja existent: " + p.getCNP());
        pacienti.put(p.getCNP(), p);
    }

    public void stergePacient(String cnp) {
        if (!pacienti.containsKey(cnp)) throw new PacientNegasitException(cnp);
        pacienti.remove(cnp);
    }

    public Optional<Pacient> cautaDupaCNP(String cnp) {
        return Optional.ofNullable(pacienti.get(cnp));
    }

    public List<Pacient> cautaDupaNume(String nume) {
        List<Pacient> result = new ArrayList<>();
        for (Pacient p : pacienti.values()) {
            if (p.getNume().equalsIgnoreCase(nume)) result.add(p);
        }
        return result;
    }

    public List<Pacient> listeazaToti() {
        List<Pacient> lista = new ArrayList<>(pacienti.values());
        Collections.sort(lista);
        return lista;
    }

    public Map<String, List<Pacient>> grupeazaDupaAsigurare() {
        Map<String, List<Pacient>> grupuri = new HashMap<>();
        for (Pacient p : pacienti.values()) {
            grupuri.computeIfAbsent(p.getTipAsigurare(), k -> new ArrayList<>()).add(p);
        }
        return grupuri;
    }

    public int getNrPacienti() { return pacienti.size(); }
}