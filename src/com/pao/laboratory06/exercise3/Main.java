package com.pao.laboratory06.exercise3;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Inginer i1 = new Inginer("Popescu", "Ion", "071111", 5000, 2000);
        Inginer i2 = new Inginer("Ionescu", "Ana", "072222", 7000, 3000);
        Inginer i3 = new Inginer("Georgescu", "Dan", null, 4000, 1500);

        Inginer[] arr = {i1, i2, i3};

        Arrays.sort(arr);
        System.out.println("Sortare dupa nume:");
        for (Inginer i : arr) System.out.println(i);

        Arrays.sort(arr, new ComparatorInginerSalariu());
        System.out.println("\nSortare dupa salariu:");
        for (Inginer i : arr) System.out.println(i);

        PlataOnline p = i1;
        p.autentificare("user", "pass");
        System.out.println("Sold: " + p.consultareSold());

        PersoanaJuridica pj = new PersoanaJuridica("Firma", "SRL", "073333", 10000);
        PlataOnlineSMS pjRef = pj;

        pjRef.trimiteSMS("Plata efectuata");
        pjRef.trimiteSMS(""); // invalid

        System.out.println("SMS-uri: " + pj.getSmsTrimise());

        PersoanaJuridica pj2 = new PersoanaJuridica("Firma2", "SRL", null, 5000);
        System.out.println("Trimite SMS fara telefon: " + pj2.trimiteSMS("Test"));


        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());


        try {
            p.autentificare(null, "pass");
        } catch (Exception e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }


        try {
            PlataOnline doarPlata = i1;

            throw new UnsupportedOperationException("Nu suporta SMS");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}