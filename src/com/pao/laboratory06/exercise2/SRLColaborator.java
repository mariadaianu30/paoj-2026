package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {
    private double cheltuieli;
    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitLunar = in.nextDouble();
        cheltuieli=in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual(){
        double net= (venitLunar-cheltuieli)*12*0.84;
        return net;
    }

    @Override
    public void afiseaza() {
        System.out.printf("SRL: %s %s, venit net anual: %.2f lei%n",
                nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "SRL";
    }

    @Override
    public TipColaborator getTip()
    {
        return TipColaborator.SRL;
    }
}
