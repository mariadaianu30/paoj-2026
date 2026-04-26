package com.pao.project.service;

import com.pao.project.model.Medic;
import com.pao.project.model.Programare;
import com.pao.project.model.Specialitate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MedicService {
    private static MedicService instance;
    private final Map<String, Medic> medici = new HashMap<>();

    private MedicService() {}

    public static MedicService getInstance() {
        if (instance == null) instance = new MedicService();
        return instance;
    }

    public void adaugaMedic(Medic m) {
        if (m == null) throw new IllegalArgumentException("Medicul nu poate fi null.");
        if (medici.containsKey(m.getParafa()))
            throw new IllegalArgumentException("Medic deja existent cu parafa: " + m.getParafa());
        medici.put(m.getParafa(), m);
    }

    public void stergeMedic(String parafa) {
        if (!medici.containsKey(parafa)) throw new IllegalArgumentException("Medic negasit: " + parafa);
        medici.remove(parafa);
    }

    public Medic cautaDupaParafa(String parafa) {
        Medic m = medici.get(parafa);
        if (m == null) throw new IllegalArgumentException("Medic negasit: " + parafa);
        return m;
    }

    public List<Medic> cautaDupaSpecialitate(Specialitate s) {
        List<Medic> result = new ArrayList<>();
        for (Medic m : medici.values()) {
            if (m.getSpecialitate() == s) result.add(m);
        }
        return result;
    }

    public List<Medic> listeazaToti() {
        List<Medic> lista = new ArrayList<>(medici.values());
        Collections.sort(lista);
        return lista;
    }

    public List<Medic> listeazaDisponibili() {
        List<Medic> result = new ArrayList<>();
        for (Medic m : medici.values()) {
            if (m.esteDisponibil()) result.add(m);
        }
        return result;
    }

    public List<Medic> listeazaDisponibiliLaOra(LocalDateTime dataOra) {
        List<Medic> result = new ArrayList<>();
        for (Medic m : medici.values()) {
            if (m.esteDisponibil()
                    && sePotrivesteOrarul(m, dataOra)
                    && !areConflictLaOra(m, dataOra)) {
                result.add(m);
            }
        }
        Collections.sort(result);
        return result;
    }

    private boolean sePotrivesteOrarul(Medic medic, LocalDateTime dataOra) {
        String program = medic.getProgram();
        if (program == null || !program.contains("-")) return true;
        try {
            String[] parts = program.split("-");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(parts[0].trim(), fmt);
            LocalTime end   = LocalTime.parse(parts[1].trim(), fmt);
            LocalTime ora   = dataOra.toLocalTime();
            return !ora.isBefore(start) && ora.isBefore(end);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean areConflictLaOra(Medic medic, LocalDateTime dataOra) {
        return medic.getProgramari().stream()
                .anyMatch(p -> p.esteActiva()
                        && Math.abs(Duration.between(p.getDataOra(), dataOra).toMinutes()) < 60);
    }

    /**
     * Punctajul unui medic (scor compozit descrescator = mai solicitat/senior):
     *   nr_programari_totale * 50  — popularitate
     *   nr_programari_active * 30  — incarcare curenta
     *   salariu / 100  — indiciu de senioritate
     */
    public int calculeazaPunctaj(Medic m) {
        int total  = m.getProgramari().size();
        long activ = m.getProgramari().stream().filter(Programare::esteActiva).count();
        return total * 50 + (int)(activ * 30) + (int)(m.getSalariu() / 100);
    }

    /** Returneaza toti medicii sortati dupa punctaj, descrescator. */
    public List<Medic> listeazaDupaRanking() {
        List<Medic> lista = new ArrayList<>(medici.values());
        Comparator<Medic> byRanking = Comparator.comparingInt(this::calculeazaPunctaj).reversed();
        lista.sort(byRanking);
        return lista;
    }

    /**
     * Returneaza, pentru fiecare specialitate prezenta in sistem,
     * lista medicilor sortata descrescator dupa punctaj.
     * Ordinea cheilor respecta ordinea declarata in enum Specialitate.
     */
    public Map<Specialitate, List<Medic>> listeazaDupaRankingPeSpecialitate() {
        Comparator<Medic> byRanking = Comparator.comparingInt(this::calculeazaPunctaj).reversed();
        Map<Specialitate, List<Medic>> result = new LinkedHashMap<>();
        for (Specialitate s : Specialitate.values()) {
            List<Medic> lista = new ArrayList<>();
            for (Medic m : medici.values()) {
                if (m.getSpecialitate() == s) lista.add(m);
            }
            if (!lista.isEmpty()) {
                lista.sort(byRanking);
                result.put(s, lista);
            }
        }
        return result;
    }


}