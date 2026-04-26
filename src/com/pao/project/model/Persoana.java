package com.pao.project.model;

import com.pao.project.interfaces.IOperatiiCitireScriere;

import java.util.Scanner;

public abstract class Persoana implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected String CNP;
    protected String adresa;

    public Persoana(String nume, String prenume, String CNP, String adresa) {
        if (nume == null || nume.isBlank()) throw new IllegalArgumentException("Numele nu poate fi gol.");
        if (prenume == null || prenume.isBlank()) throw new IllegalArgumentException("Prenumele nu poate fi gol.");
        if (CNP == null || CNP.length() != 13) throw new IllegalArgumentException("CNP-ul trebuie sa aiba exact 13 caractere.");
        this.nume = nume;
        this.prenume = prenume;
        this.CNP = CNP;
        this.adresa = adresa;
    }

    protected Persoana() {
        this.nume = "";
        this.prenume = "";
        this.CNP = "";
        this.adresa = "";
    }

    public abstract String getRol();

    public String getNumeComplet() { return prenume + " " + nume; }

    @Override
    public void citeste(Scanner in) {
        System.out.print("Nume: ");
        this.nume = in.nextLine();
        System.out.print("Prenume: ");
        this.prenume = in.nextLine();
        System.out.print("CNP (13 caractere): ");
        this.CNP = in.nextLine();
        System.out.print("Adresa: ");
        this.adresa = in.nextLine();
    }

    @Override
    public void afiseaza() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "[" + getRol() + "] " + getNumeComplet() + " (CNP: " + CNP + ")";
    }

    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getCNP() { return CNP; }
    public String getAdresa() { return adresa; }
    public void setNume(String nume) { this.nume = nume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
}