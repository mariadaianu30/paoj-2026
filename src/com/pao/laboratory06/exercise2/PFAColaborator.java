package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieli;

    private static final double VENIT_MINIM=4050;
    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitLunar = in.nextDouble();
        cheltuieli=in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net= (venitLunar-cheltuieli)*12;
        double venitBrutAnual=venitLunar*12;
        double impozit=net*0.10;


        double cass;
        if (venitBrutAnual < 6 * VENIT_MINIM * 12)
            cass = 0.10 * (6 * VENIT_MINIM * 12);
        else if (venitBrutAnual <= 72 * VENIT_MINIM * 12)
            cass = 0.10 * net;
        else
            cass = 0.10 * (72 * VENIT_MINIM * 12);

// CAS
        double cas;
        if (venitBrutAnual < 12 * VENIT_MINIM * 12)
            cas = 0;
        else if (venitBrutAnual <= 24 * VENIT_MINIM * 12)
            cas = 0.25 * (12 * VENIT_MINIM * 12);
        else
            cas = 0.25 * (24 * VENIT_MINIM * 12);

        double net_anual=net-impozit-cas-cass;

        return net_anual;
    }

    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei%n",
                nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public TipColaborator getTip()
    {
        return TipColaborator.PFA;
    }

}
