package com.pao.laboratory11.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    // Tari considerate cu risc ridicat
    private static final Set<String> HIGH_RISK_COUNTRIES =
            new HashSet<>(Arrays.asList("RU", "NG", "IR", "KP", "SY"));

    // Punctaj de risc asociat fiecarui canal de plata.

    private static final Map<String, Integer> CHANNEL_SCORE = new HashMap<>();

    static {
        CHANNEL_SCORE.put("WEB", 15);
        CHANNEL_SCORE.put("APP", 10);
        CHANNEL_SCORE.put("CRYPTO", 30);
        CHANNEL_SCORE.put("POS", 5);
        CHANNEL_SCORE.put("ATM", 0);
    }

    // Sorteaza tranzactiile dupa scor descrescator; la scor egal, id-ul mai mic are prioritate.
    private static final Comparator<Transaction> BY_RISK_DESC_THEN_ID_ASC =
            Comparator.comparingInt(Main::riskScore).reversed().thenComparingInt(t -> t.id);

    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            // Eroare de I/O
            System.out.println("ERR IO");
        }
    }

    private static void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // Prima linie nevida contine numarul de tranzactii.
        String first = readNonEmptyLine(br);
        if (first == null) {
            return;
        }

        int n = Integer.parseInt(first);
        Map<Integer, Transaction> byId = new HashMap<>(); // pentru cautare rapida dupa id
        List<Transaction> all = new ArrayList<>();         // pentru iterare si sortare

        // Citim cele n tranzactii, fiecare pe cate o linie cu 5 campuri separate prin spatiu.
        for (int i = 0; i < n; i++) {
            String line = readNonEmptyLine(br);
            if (line == null) {
                return;
            }

            String[] tok = line.split("\\s+");
            if (tok.length < 5) {
                continue;
            }

            // tok[0]=id, tok[1]=suma, tok[2]=data, tok[3]=tara, tok[4]=canal
            Transaction tx = new Transaction(
                    Integer.parseInt(tok[0]),
                    Double.parseDouble(tok[1]),
                    tok[2],
                    tok[3].toUpperCase(),
                    tok[4].toUpperCase());

            byId.put(tx.id, tx);
            all.add(tx);
        }

        // Urmatoarea linie nevida contine numarul de comenzi.
        String qLine = readNonEmptyLine(br);
        if (qLine == null) {
            return;
        }
        int q = Integer.parseInt(qLine);

        for (int i = 0; i < q; i++) {
            String cmdLine = readNonEmptyLine(br);
            if (cmdLine == null) {
                return;
            }

            String[] cmd = cmdLine.split("\\s+");
            String op = cmd[0].toUpperCase();

            switch (op) {

                // CHECK <id> — afiseaza verdictul si scorul pentru tranzactia cu id-ul dat.
                case "CHECK":
                    if (cmd.length < 2) {
                        System.out.println("ERR BAD_COMMAND");
                        break;
                    }
                    int id = Integer.parseInt(cmd[1]);
                    Transaction tx = byId.get(id);
                    if (tx == null) {
                        System.out.println("CHECK " + id + " => NOT_FOUND");
                    } else {
                        int score = riskScore(tx);
                        System.out.println("CHECK " + id + " => " + verdict(score) + " score=" + score);
                    }
                    break;

                // LIST_FLAGGED — listeaza toate tranzactiile cu scor >= 60, sortate dupa risc descrescator.
                case "LIST_FLAGGED":
                    List<Transaction> flagged = new ArrayList<>();
                    for (Transaction t : all) {
                        if (isFlagged(t)) {
                            flagged.add(t);
                        }
                    }
                    flagged.sort(BY_RISK_DESC_THEN_ID_ASC);
                    if (flagged.isEmpty()) {
                        System.out.println("NONE");
                    } else {
                        for (Transaction t : flagged) {
                            System.out.println(formatRiskLine(t));
                        }
                    }
                    break;

                // TOP_RISK <k> — afiseaza primele k tranzactii cu cel mai mare scor de risc.
                case "TOP_RISK":
                    if (cmd.length < 2) {
                        System.out.println("ERR BAD_COMMAND");
                        break;
                    }
                    int k = Integer.parseInt(cmd[1]);
                    List<Transaction> ranked = new ArrayList<>(all);
                    ranked.sort(BY_RISK_DESC_THEN_ID_ASC);

                    int limit = Math.max(0, Math.min(k, ranked.size()));
                    for (int idx = 0; idx < limit; idx++) {
                        System.out.println(formatRiskLine(ranked.get(idx)));
                    }
                    break;

                default:
                    System.out.println("ERR UNKNOWN_COMMAND");
                    break;
            }
        }
    }

    // Citeste linii pana gaseste una nevida, sarind peste liniile goale sau cu spatii.
    private static String readNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return null;
    }

    // Calculeaza scorul de risc al unei tranzactii pe baza sumei, tarii si canalului.
    private static int riskScore(Transaction tx) {

        int score = 0;

        // Sumele mari sunt mai suspecte — pragurile reflecta limite uzuale de raportare AML.
        if (tx.amount >= 5000.0) {
            score += 70;
        } else if (tx.amount >= 1000.0) {
            score += 40;
        } else if (tx.amount >= 500.0) {
            score += 20;
        }

        // Sumele foarte mici pot indica "structurare" (fragmentarea intentionata a tranzactiilor).
        if (tx.amount <= 100.0) {
            score += 5;
        }

        if (HIGH_RISK_COUNTRIES.contains(tx.country)) {
            score += 25;
        }

        score += CHANNEL_SCORE.getOrDefault(tx.channel, 0);
        return score;
    }


    private static boolean isFlagged(Transaction tx) {
        return riskScore(tx) >= 60;
    }

    private static String verdict(int score) {
        return score >= 60 ? "FLAG" : "ALLOW";
    }

    private static String formatRiskLine(Transaction tx) {
        int score = riskScore(tx);
        return "[" + tx.id + "] " + verdict(score) + " score=" + score;
    }

    // Clasa interna privata — folosita doar pentru citire, de aceea campurile sunt package-private.
    private static class Transaction {
        private final int id;
        private final double amount;
        private final String date;
        private final String country;
        private final String channel;

        private Transaction(int id, double amount, String date, String country, String channel) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
        }
    }
}