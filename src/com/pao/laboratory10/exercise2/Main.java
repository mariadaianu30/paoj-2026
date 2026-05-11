package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();

        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < N; i++) {

            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            lista.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {

            String comanda = scanner.next();

            switch (comanda) {

                case "UNIQUE_IDS": {

                    LinkedHashSet<Integer> set = new LinkedHashSet<>();

                    for (Tranzactie t : lista) {
                        set.add(t.getId());
                    }

                    System.out.println("IDs unice (" + set.size() + "): " + set);

                    break;
                }

                case "MONTHLY_REPORT": {

                    TreeMap<String, double[]> raport = new TreeMap<>();

                    for (Tranzactie t : lista) {

                        String luna = t.getData().substring(0, 7);

                        raport.putIfAbsent(luna, new double[2]);

                        if (t.getTip() == TipTranzactie.CREDIT) {
                            raport.get(luna)[0] += t.getSuma();
                        } else {
                            raport.get(luna)[1] += t.getSuma();
                        }
                    }

                    for (Map.Entry<String, double[]> entry : raport.entrySet()) {

                        String luna = entry.getKey();

                        double credit = entry.getValue()[0];
                        double debit = entry.getValue()[1];

                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                luna, credit, debit);
                    }

                    break;
                }

                case "TOP": {

                    int n = scanner.nextInt();

                    List<Tranzactie> copie = new ArrayList<>(lista);

                    copie.sort(
                            Comparator.comparingDouble(Tranzactie::getSuma)
                                    .reversed()
                    );

                    System.out.println("Top " + n + ":");

                    for (int i = 0; i < Math.min(n, copie.size()); i++) {
                        System.out.println(copie.get(i));
                    }

                    break;
                }

                case "SORT_ASC": {

                    lista.sort(
                            Comparator.comparingDouble(Tranzactie::getSuma)
                    );

                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }

                    break;
                }

                case "SORT_DESC": {

                    lista.sort(
                            Comparator.comparingDouble(Tranzactie::getSuma)
                                    .reversed()
                    );

                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }

                    break;
                }

                case "REVERSE": {

                    Collections.reverse(lista);

                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }

                    break;
                }

                case "MIN_MAX": {

                    Tranzactie min = Collections.min(
                            lista,
                            Comparator.comparingDouble(Tranzactie::getSuma)
                    );

                    Tranzactie max = Collections.max(
                            lista,
                            Comparator.comparingDouble(Tranzactie::getSuma)
                    );

                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);

                    break;
                }

                case "CME_DEMO": {

                    try {

                        for (Tranzactie t : lista) {
                            lista.remove(t);
                        }

                    } catch (ConcurrentModificationException e) {

                        System.out.println(
                                "ConcurrentModificationException prins: modificare in iteratie detectata."
                        );
                    }

                    break;
                }
            }
        }

        scanner.close();
    }
}