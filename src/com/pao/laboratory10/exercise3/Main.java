package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.Collectors;

enum TipTranzactie {
    CREDIT,
    DEBIT
}

class Tranzactie {

    private int id;
    private double suma;
    private String data;
    private TipTranzactie tip;
    private String contSursa;

    public Tranzactie(int id,
                      double suma,
                      String data,
                      TipTranzactie tip,
                      String contSursa) {

        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
        this.contSursa = contSursa;
    }

    public int getId() {
        return id;
    }

    public double getSuma() {
        return suma;
    }

    public String getData() {
        return data;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    public String getContSursa() {
        return contSursa;
    }

    @Override
    public String toString() {
        return String.format(
                "[%d] %s %s: %.2f RON (%s)",
                id,
                data,
                tip,
                suma,
                contSursa
        );
    }
}

public class Main {

    public static void main(String[] args) {

        List<Tranzactie> tranzactii = Arrays.asList(

                new Tranzactie(1, 1500, "2024-01-10",
                        TipTranzactie.CREDIT, "CONT_A"),

                new Tranzactie(2, 750, "2024-01-15",
                        TipTranzactie.DEBIT, "CONT_B"),

                new Tranzactie(3, 2200, "2024-01-20",
                        TipTranzactie.CREDIT, "CONT_A"),

                new Tranzactie(4, 400, "2024-02-02",
                        TipTranzactie.DEBIT, "CONT_C"),

                new Tranzactie(5, 1800, "2024-02-10",
                        TipTranzactie.CREDIT, "CONT_D"),

                new Tranzactie(6, 950, "2024-02-18",
                        TipTranzactie.DEBIT, "CONT_B"),

                new Tranzactie(7, 3000, "2024-03-01",
                        TipTranzactie.CREDIT, "CONT_E"),

                new Tranzactie(8, 1200, "2024-03-05",
                        TipTranzactie.DEBIT, "CONT_A"),

                new Tranzactie(9, 500, "2024-03-12",
                        TipTranzactie.CREDIT, "CONT_F"),

                new Tranzactie(10, 850, "2024-03-20",
                        TipTranzactie.DEBIT, "CONT_C")
        );

        // 1. FILTER CREDIT
        System.out.println("=== TRANZACTII CREDIT ===");

        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);


        // 2. TOTAL SUMA
        System.out.println("\n=== TOTAL PROCESAT ===");

        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();

        System.out.printf("Total procesat: %.2f RON%n", total);


        // 3. GROUP BY MONTH

        System.out.println("\n=== TOTAL PER LUNA ===");

        Map<String, Double> totalPerLuna =
                tranzactii.stream()
                        .collect(
                                Collectors.groupingBy(
                                        t -> t.getData().substring(0, 7),
                                        TreeMap::new,
                                        Collectors.summingDouble(
                                                Tranzactie::getSuma
                                        )
                                )
                        );

        totalPerLuna.forEach(
                (luna, suma) ->
                        System.out.printf("%s: %.2f RON%n",
                                luna,
                                suma)
        );

        // 4. TOP 3

        System.out.println("\n=== TOP 3 TRANZACTII ===");

        tranzactii.stream()
                .sorted(
                        Comparator.comparingDouble(
                                Tranzactie::getSuma
                        ).reversed()
                )
                .limit(3)
                .forEach(System.out::println);


        // 5. CONTURI UNICE


        System.out.println("\n=== CONTURI SURSA UNICE ===");

        List<String> conturiUnice =
                tranzactii.stream()
                        .map(Tranzactie::getContSursa)
                        .distinct()
                        .collect(Collectors.toList());

        System.out.println(conturiUnice);

        // 6. MEDIA SUMELOR


        System.out.println("\n=== SUMA MEDIE ===");

        double media = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);

        System.out.printf("Suma medie: %.2f RON%n", media);

        // 7. EXTRAS DE CONT PE LUNI


        System.out.println("\n=== EXTRAS DE CONT ===");

        Map<String, List<Tranzactie>> extras =
                tranzactii.stream()
                        .collect(
                                Collectors.groupingBy(
                                        t -> t.getData().substring(0, 7),
                                        TreeMap::new,
                                        Collectors.toList()
                                )
                        );

        extras.forEach((luna, lista) -> {

            double sumaTotala = lista.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();

            System.out.printf(
                    "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna,
                    lista.size(),
                    sumaTotala
            );
        });
    }
}