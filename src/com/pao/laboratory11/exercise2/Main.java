package com.pao.laboratory11.exercise2;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int N = Integer.parseInt(scanner.nextLine().trim());

        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < N; i++) {

            String line = scanner.nextLine().trim();

            while (line.isEmpty()) {
                line = scanner.nextLine().trim();
            }

            String[] parts = line.split("\\s+");

            int id = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            String date = parts[2];
            String country = parts[3];
            String channel = parts[4];
            String accountId = parts[5];

            transactions.add(
                    new Transaction(
                            id,
                            amount,
                            date,
                            country,
                            channel,
                            accountId
                    )
            );
        }

        int Q = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 0; i < Q; i++) {

            String line = scanner.nextLine().trim();

            while (line.isEmpty()) {
                line = scanner.nextLine().trim();
            }

            String[] cmd = line.split("\\s+");

            String command = cmd[0];

            switch (command) {
                case "REPORT_MONTH": {

                    // cmd[1] e luna in formatul "YYYY-MM"; filtram dupa startsWith ca data e "YYYY-MM-DD"
                    String month = cmd[1];

                    // doua stream-uri separate pe aceeasi lista — unul pentru suma, unul pentru numarare
                    double total = transactions.stream()
                            .filter(tx ->
                                    tx.date.startsWith(month))
                            .mapToDouble(tx -> tx.amount)
                            .sum();

                    long count = transactions.stream()
                            .filter(tx ->
                                    tx.date.startsWith(month))
                            .count();

                    // Locale.US asigura ca separatorul zecimal e "." indiferent de setarile sistemului
                    System.out.printf(
                            Locale.US,
                            "MONTH %s total=%.2f count=%d%n",
                            month,
                            total,
                            count
                    );

                    break;
                }

                case "REPORT_ACCOUNT": {

                    String accountId = cmd[1];

                    double total = transactions.stream()
                            .filter(tx ->
                                    tx.accountId.equals(accountId))
                            .mapToDouble(tx -> tx.amount)
                            .sum();

                    long count = transactions.stream()
                            .filter(tx ->
                                    tx.accountId.equals(accountId))
                            .count();

                    System.out.printf(
                            Locale.US,
                            "ACCOUNT %s total=%.2f count=%d%n",
                            accountId,
                            total,
                            count
                    );

                    break;
                }

                case "TOP_CHANNELS": {

                    int k = Integer.parseInt(cmd[1]);

                    if (transactions.isEmpty()) {

                        System.out.println("NONE");
                        break;
                    }

                    // groupingBy + counting => map canal -> numar de aparitii
                    Map<String, Long> counts =
                            transactions.stream()
                                    .collect(
                                            Collectors.groupingBy(
                                                    tx -> tx.channel,
                                                    Collectors.counting()
                                            )
                                    );

                    // sortare: descrescator dupa numar de tranzactii, iar la egalitate alfabetic dupa nume canal
                    counts.entrySet()
                            .stream()
                            .sorted(
                                    Comparator
                                            .<Map.Entry<String, Long>>
                                                    comparingLong(
                                                    Map.Entry::getValue
                                            )
                                            .reversed()
                                            .thenComparing(
                                                    Map.Entry::getKey
                                            )
                            )
                            .limit(k)
                            .forEach(e ->
                                    System.out.println(
                                            e.getKey()
                                                    + " "
                                                    + e.getValue()
                                    )
                            );

                    break;
                }

                default:
                    break;
            }
        }

        scanner.close();
    }
}