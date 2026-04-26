package com.pao.project.model;

import com.pao.project.exception.MedicNedisponibilException;
import com.pao.project.interfaces.Consultabil;
import com.pao.project.interfaces.Programabil;

import java.time.Duration;
import java.util.*;

public class Medic extends Angajat implements Consultabil, Programabil, Comparable<Medic> {
    private final String parafa;
    private Specialitate specialitate;
    private boolean disponibil;
    private final List<Programare> programari;

    public Medic(String idAngajat, String nume, String prenume, String CNP, String adresa,
                 double salariu, String program, String parafa, Specialitate specialitate) {
        super(idAngajat, nume, prenume, CNP, adresa, salariu, program);
        this.parafa = parafa;
        this.specialitate = specialitate;
        this.disponibil = true;
        this.programari = new ArrayList<>();
    }

    @Override
    public String getRol() { return "Medic " + specialitate.getDenumire(); }

    @Override
    public Consultatie consulta(Pacient pacient, String simptome) {
        if (!disponibil) throw new MedicNedisponibilException(getNumeComplet());
        Consultatie c = new Consultatie(this, pacient, simptome);
        pacient.getFisaMedicala().adaugaConsultatie(c);
        return c;
    }

    @Override
    public Reteta emiteReteta(Consultatie consultatie, String[] medicamente) {
        if (consultatie == null) throw new IllegalArgumentException("Consultatia nu poate fi null.");
        if (medicamente == null || medicamente.length == 0)
            throw new IllegalArgumentException("Lista de medicamente nu poate fi goala.");
        Reteta reteta = new Reteta(this, consultatie.getPacient(), medicamente);
        consultatie.setReteta(reteta);
        return reteta;
    }

    @Override
    public boolean esteDisponibil() { return disponibil; }

    @Override
    public void adaugaProgramare(Programare programare) {
        if (programare == null) throw new IllegalArgumentException("Programarea nu poate fi null.");
        programari.add(programare);
    }

    @Override
    public void anuleazaProgramare(String idProgramare) {
        for (Programare p : programari) {
            if (p.getIdProgramare().equals(idProgramare)) {
                p.anuleaza();
                return;
            }
        }
        throw new IllegalArgumentException("Programare negasita: " + idProgramare);
    }

    @Override
    public boolean areConflictOrar(Programare programare) {
        return programari.stream()
                .anyMatch(x -> x.esteActiva()
                        && Math.abs(Duration.between(x.getDataOra(), programare.getDataOra()).toMinutes()) < 60);
    }

    @Override
    public int compareTo(Medic alt) {
        int cmp = this.getNume().compareTo(alt.getNume());
        return cmp != 0 ? cmp : this.getPrenume().compareTo(alt.getPrenume());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medic other)) return false;
        return parafa.equals(other.parafa);
    }

    @Override
    public int hashCode() { return parafa.hashCode(); }

    @Override
    public String toString() {
        return "Medic{nume='" + getNumeComplet() + "', specialitate='" + specialitate.getDenumire()
                + "', parafa='" + parafa + "', disponibil=" + disponibil + "}";
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        Specialitate[] vals = Specialitate.values();
        System.out.println("Specialitati disponibile:");
        for (int i = 0; i < vals.length; i++) {
            System.out.println("  " + i + ". " + vals[i].getDenumire());
        }
        System.out.print("Alegeti specialitatea (index): ");
        int idx = Integer.parseInt(in.nextLine().trim());
        this.specialitate = vals[idx];
        this.disponibil = true;
    }

    @Override
    public void afiseaza() {
        System.out.println(toString());
        System.out.println("  Programari:");
        for (Programare p : programari) {
            System.out.print("    ");
            p.afiseaza();
        }
    }

    public String getParafa() { return parafa; }
    public Specialitate getSpecialitate() { return specialitate; }
    public void setSpecialitate(Specialitate specialitate) { this.specialitate = specialitate; }
    public void setDisponibil(boolean disponibil) { this.disponibil = disponibil; }
    public List<Programare> getProgramari() { return Collections.unmodifiableList(programari); }
}