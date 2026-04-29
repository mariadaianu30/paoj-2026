package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        String cale = "src/com/pao/laboratory08/tests/studenti.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(cale))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                linie = linie.trim();
                if (linie.isEmpty()) continue;

                String[] parts = linie.split(",");
                String nume   = parts[0].trim();
                int varsta    = Integer.parseInt(parts[1].trim());
                String oras   = parts[2].trim();
                String strada = parts[3].trim();

                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return studenti;
    }

    private static Student gaseste(List<Student> studenti, String nume) {
        for (Student s : studenti) {
            if (s.getNume().equals(nume)) return s;
        }
        return null;
    }

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        System.out.println("-------Alegeti o comanda------------");
        System.out.println("1.PRINT ");
        System.out.println("2. SHALLOW + nume");
        System.out.println("3. DEEP + nume");

        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine().trim();
        scanner.close();

        List<Student> studenti = citesteStudenti();

        if (comanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }

        } else if (comanda.startsWith("SHALLOW ")) {
            String nume = comanda.split(" ", 2)[1].trim();
            Student original = gaseste(studenti, nume);
            if (original == null) {
                System.out.println("Studentul nu a fost gasit: " + nume);
                return;
            }

            Student clona = original.shallowClone();
            clona.getAdresa().setOras("MODIFICAT"); // afecteaza si originalul!

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);

        } else if (comanda.startsWith("DEEP ")) {
            String nume = comanda.split(" ", 2)[1].trim();
            Student original = gaseste(studenti, nume);
            if (original == null) {
                System.out.println("Studentul nu a fost gasit: " + nume);
                return;
            }

            Student clona = original.deepClone();
            clona.getAdresa().setOras("MODIFICAT"); // nu afecteaza originalul

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);

        } else {
            System.out.println("Comanda necunoscuta: " + comanda);
        }
    }
}