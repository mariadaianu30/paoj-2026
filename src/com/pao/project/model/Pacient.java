package com.pao.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Pacient extends Persoana implements Comparable<Pacient> {
    private CodPacient codPacient;
    private LocalDate dataInscriere;
    private String tipAsigurare;
    private FisaMedicala fisaMedicala;

    public Pacient(CodPacient cod, String nume, String prenume, String CNP, String adresa, String tipAsigurare) {
        super(nume, prenume, CNP, adresa);
        this.codPacient = cod;
        this.dataInscriere = LocalDate.now();
        this.tipAsigurare = tipAsigurare;
        this.fisaMedicala = new FisaMedicala(this);
    }

    public Pacient() {
        super();
        this.dataInscriere = LocalDate.now();
    }

    @Override
    public String getRol() { return "Pacient"; }

    @Override
    public int compareTo(Pacient alt) { return this.dataInscriere.compareTo(alt.dataInscriere); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pacient other)) return false;
        return CNP.equals(other.CNP);
    }

    @Override
    public int hashCode() { return CNP.hashCode(); }

    @Override
    public String toString() {
        String cod = codPacient != null ? codPacient.getCod() : "N/A";
        String data = dataInscriere.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return "Pacient{cod='" + cod + "', nume='" + getNumeComplet()
                + "', inscriere='" + data + "', asigurare='" + tipAsigurare + "'}";
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        System.out.print("Tip asigurare (CNAS/privat): ");
        this.tipAsigurare = in.nextLine();
        this.codPacient = new CodPacient("PAC-" + String.format("%04d", (int) (Math.random() * 9999)));
        this.fisaMedicala = new FisaMedicala(this);
    }

    @Override
    public void afiseaza() {
        System.out.println(toString());
        fisaMedicala.afiseaza();
    }

    public CodPacient getCodPacient() { return codPacient; }
    public LocalDate getDataInscriere() { return dataInscriere; }
    public String getTipAsigurare() { return tipAsigurare; }
    public FisaMedicala getFisaMedicala() { return fisaMedicala; }
    public void setTipAsigurare(String tipAsigurare) { this.tipAsigurare = tipAsigurare; }
}