package com.pao.laboratory06.exercise3;

import java.util.*;
public class PersoanaJuridica extends Persoana implements PlataOnlineSMS{
    private double sold;

    private List<String> smsTrimise= new ArrayList<>();

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold)
    {
        super(nume, prenume, telefon);
        this.sold=sold;
    }

    @Override
    public void autentificare(String user, String parola)
    {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("Date invalide");
        }
        System.out.println("PJ autentificata");
    }

    @Override
    public double consultareSold() {
        return sold;
    }
    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) throw new IllegalArgumentException("Suma invalida");

        if (suma <= sold)
        {
            sold-= suma;
            return true;
        }
        return false;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) return false;
        if (telefon == null || telefon.isEmpty()) return false;
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }



}
