package com.pao.project.model;

import java.util.Scanner;

public class Asistent extends Angajat {
    private String grad;
    private String certificat;

    public Asistent(String idAngajat, String nume, String prenume, String CNP, String adresa, double salariu, String program, String grad, String certificat) {
        super(idAngajat, nume, prenume, CNP, adresa, salariu, program);
        this.grad = grad;
        this.certificat = certificat;
    }

    @Override
    public String getRol() { return "Asistent medical"; }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        System.out.print("Grad (ex: gradul I): ");
        this.grad = in.nextLine();
        System.out.print("Certificat: ");
        this.certificat = in.nextLine();
    }

    @Override
    public void afiseaza() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Asistent{nume='" + getNumeComplet() + "', grad='" + grad + "', certificat='" + certificat + "'}";
    }

    public String getGrad() { return grad; }
    public String getCertificat() { return certificat; }
    public void setGrad(String grad) { this.grad = grad; }
    public void setCertificat(String certificat) { this.certificat = certificat; }
}