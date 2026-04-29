package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int prag = Integer.parseInt(scanner.nextLine().trim());
        scanner.close();

        List<Student> studenti = citesteStudenti();

        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rezultate.txt"))) {
            for (Student s : filtrati) {
                bw.write(s.toString());
                bw.newLine();
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student s : filtrati) {
            System.out.println(s);
        }

    }
}