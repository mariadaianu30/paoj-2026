package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajati =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta dupa departament");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine(); 

            switch (optiune) {
                case 1:
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();

                    System.out.print("Departament: ");
                    String numeDept = scanner.nextLine();

                    System.out.print("Locatie departament: ");
                    String locatie = scanner.nextLine();

                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    scanner.nextLine();

                    Departament dept = new Departament(numeDept, locatie);
                    Angajat angajat = new Angajat(nume, dept, salariu);

                    service.addAngajat(angajat);
                    break;

                case 2:
                    System.out.println("\n--- Angajati dupa salariu ---");
                    service.listBySalary();
                    break;

                case 3:
                    System.out.print("Departament cautat: ");
                    String cautare = scanner.nextLine();
                    service.findByDepartament(cautare);
                    break;

                case 0:
                    System.out.println("Iesire...");
                    return;

                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }
}