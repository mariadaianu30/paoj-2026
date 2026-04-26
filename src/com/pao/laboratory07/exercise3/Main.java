package com.pao.laboratory07.exercise3;

import java.text.DecimalFormat;import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");

            if (tokens[0].equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client = tokens[3];
                Comanda c = new ComandaStandard(nume, client, pret);
                comenzi.add(c);

            } else if (tokens[0].equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client = tokens[4];
                Comanda c = new ComandaRedusa(nume,client, pret, discount);
                comenzi.add(c);

            } else if (tokens[0].equals("GIFT")) {
                String nume = tokens[1];
                String client = tokens[2];
                Comanda c = new ComandaGratuita(nume,client);

                comenzi.add(c);

            }
        }
        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        System.out.println();
        while(sc.hasNextLine())
        {
            String line = sc.nextLine().trim();
            if (line.equals("QUIT")) break;

            if(line.equals("STATS"))
            {
                System.out.println("---- STATS ----");
                Map<String, Double> orders= comenzi.stream().collect(Collectors.groupingBy(Comanda::getTip, Collectors.averagingDouble(Comanda::pretFinal)));

                orders.forEach((tip, medie)-> System.out.printf("%s: medie = %.2f lei%n", tip, medie));
                System.out.println();
            }

            else if(line.startsWith("FILTER")){

                double threshold = Double.parseDouble(line.split(" ")[1]);

                System.out.printf("--- FILTER (>= %.2f) ---\n", threshold);

                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.printf("%s: %s, pret: %s - client: %s%n",
                                c.getTip(), c.nume,
                                c instanceof ComandaGratuita ? "gratuit" : new DecimalFormat("0.00").format(c.pretFinal()) + " lei",
                                c.client));

                System.out.println();
            }

         else if (line.equals("SORT")) {
            System.out.println("--- SORT (by client, then by pret) ---");

            comenzi.stream(). sorted(Comparator.comparing((Comanda c)->c.client).thenComparing((Comanda c)->c.pretFinal()))
                    .forEach(c -> System.out.printf("%s: %s, pret: %s - client: %s%n",
                            c.getTip(), c.nume,
                            c instanceof ComandaGratuita ? "gratuit" : new DecimalFormat("0.00").format(c.pretFinal()) + " lei",
                            c.client));

            System.out.println();

        }

         else if(line.equals("SPECIAL"))
            {
                System.out.println("--- SPECIAL (discount > 15%) ---");

                comenzi.stream().filter((Comanda c)->c instanceof ComandaRedusa && ((ComandaRedusa) c).getDiscount()>15)
                        .forEach(c -> System.out.printf("%s: %s, pret: %s - client: %s%n",
                                c.getTip(), c.nume,
                                c instanceof ComandaGratuita ? "gratuit" : new DecimalFormat("0.00").format(c.pretFinal()) + " lei",
                                c.client));
                System.out.println();

        }

    }
        sc.close();
    }
}
