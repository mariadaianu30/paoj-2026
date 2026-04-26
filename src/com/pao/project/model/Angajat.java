package com.pao.project.model;

import java.util.Scanner;

public abstract class Angajat extends Persoana {
    private String idAngajat;
    private double salariu;
    private String program;

    public Angajat(String idAngajat, String nume, String prenume, String CNP, String adresa, double salariu, String program) {
        super(nume, prenume, CNP, adresa);
        if (salariu < 0) throw new IllegalArgumentException("Salariul nu poate fi negativ.");
        this.idAngajat = idAngajat;
        this.salariu = salariu;
        this.program = program;
    }

    protected Angajat() {
        super();
    }

    public abstract String getRol();

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        System.out.print("ID Angajat: ");
        this.idAngajat = in.nextLine();
        System.out.print("Salariu: ");
        this.salariu = Double.parseDouble(in.nextLine().trim());
        System.out.print("Program (ex: 08:00-16:00): ");
        this.program = in.nextLine();
    }

    @Override
    public void afiseaza() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return super.toString() + " [ID: " + idAngajat + ", Salariu: " + salariu + " RON]";
    }

    public String getIdAngajat() { return idAngajat; }
    public double getSalariu() { return salariu; }
    public String getProgram() { return program; }
    public void setIdAngajat(String idAngajat) { this.idAngajat = idAngajat; }
    public void setSalariu(double salariu) {
        if (salariu < 0) throw new IllegalArgumentException("Salariul nu poate fi negativ.");
        this.salariu = salariu;
    }
    public void setProgram(String program) { this.program = program; }
}