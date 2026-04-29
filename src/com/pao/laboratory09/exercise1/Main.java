package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String FILE_PATH = "output/lab09_ex1.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        // Read N transactions
        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.setNote("procesat");
            tranzactii.add(t);
        }

        // Serialize
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(tranzactii);
        }

        // Deserialize
        List<Tranzactie> deserializate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            deserializate = (List<Tranzactie>) ois.readObject();
        }

        // Process commands until EOF
        while (scanner.hasNextLine()) {
            String linie = scanner.nextLine().trim();
            if (linie.isEmpty()) continue;

            if (linie.equals("LIST")) {
                for (Tranzactie t : deserializate) {
                    System.out.println(t);
                }
            } else if (linie.startsWith("FILTER ")) {
                String prefix = linie.substring(7).trim();
                List<Tranzactie> filtrate = new ArrayList<>();
                for (Tranzactie t : deserializate) {
                    if (t.getData().startsWith(prefix)) {
                        filtrate.add(t);
                    }
                }
                if (filtrate.isEmpty()) {
                    System.out.println("Niciun rezultat.");
                } else {
                    for (Tranzactie t : filtrate) {
                        System.out.println(t);
                    }
                }
            } else if (linie.startsWith("NOTE ")) {
                int targetId = Integer.parseInt(linie.substring(5).trim());
                Tranzactie gasita = null;
                for (Tranzactie t : deserializate) {
                    if (t.getId() == targetId) {
                        gasita = t;
                        break;
                    }
                }
                if (gasita == null) {
                    System.out.println("NOTE[" + targetId + "]: not found");
                } else {
                    System.out.println("NOTE[" + targetId + "]: " + gasita.getNote());
                }
            }
        }
    }
}