package com.pao.project;

import com.pao.project.exception.MedicNedisponibilException;
import com.pao.project.exception.PacientNegasitException;
import com.pao.project.exception.ProgramareConflictException;
import com.pao.project.model.*;
import com.pao.project.service.MedicService;
import com.pao.project.service.PacientService;
import com.pao.project.service.ProgramareService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Meniu {
    private static Meniu instance;
    private final Scanner scanner = new Scanner(System.in);
    private final PacientService    pacientService    = PacientService.getInstance();
    private final MedicService      medicService      = MedicService.getInstance();
    private final ProgramareService programareService = ProgramareService.getInstance();

    private Meniu() { initializeazaDate(); }

    public static Meniu getInstance() {
        if (instance == null) instance = new Meniu();
        return instance;
    }

    public void ruleaza() {
        int optiune;
        do {
            afiseazaMeniu();
            optiune = citestOptiune();
            proceseazaOptiune(optiune);
        } while (optiune != 0);
    }

    private void afiseazaMeniu() {
        System.out.println("\n========== CABINET MEDICAL ==========");
        System.out.println(" 1. Inregistreaza pacient");
        System.out.println(" 2. Adauga medic");
        System.out.println(" 3. Creeaza programare");
        System.out.println(" 4. Anuleaza programare");
        System.out.println(" 5. Efectueaza consultatie");
        System.out.println(" 6. Emite reteta");
        System.out.println(" 7. Cauta pacient (dupa CNP)");
        System.out.println(" 8. Programarile unui medic");
        System.out.println(" 9. Fisa medicala pacient");
        System.out.println("10. Sterge pacient");
        System.out.println("11. Programarile unui pacient");
        System.out.println("12. Ranking medici");
        System.out.println("13. Pacienti pe tip asigurare");
        System.out.println("14. Adauga analiza la pacient");
        System.out.println(" 0. Iesire");
        System.out.println("=====================================");
        System.out.print("Optiunea ta: ");
    }

    private int citestOptiune() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void proceseazaOptiune(int opt) {
        switch (opt) {
            case 1  -> inregistreazaPacient();
            case 2  -> adaugaMedic();
            case 3  -> creeazaProgramare();
            case 4  -> anuleazaProgramare();
            case 5  -> efectueazaConsultatie();
            case 6  -> emiteReteta();
            case 7  -> cautaPacient();
            case 8  -> listeazaProgramariMedic();
            case 9  -> afiseazaFisaMedicala();
            case 10 -> stergePacient();
            case 11 -> listeazaProgramariPacient();
            case 12 -> listeazaMediciDupaRanking();
            case 13 -> afiseazaPacientiPeAsigurare();
            case 14 -> adaugaAnalizaPacient();
            case 0  -> System.out.println("La revedere!");
            default -> System.out.println("Optiune invalida. Incercati din nou.");
        }
    }


    //  1. Inregistreaza pacient

    private void inregistreazaPacient() {
        Pacient p = new Pacient();
        p.citeste(scanner);
        try {
            pacientService.adaugaPacient(p);
            System.out.println("Pacient inregistrat: " + p.getNumeComplet());
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  2. Adauga medic

    private void adaugaMedic() {
        try {
            System.out.print("ID Angajat: ");       String idAngajat = scanner.nextLine();
            System.out.print("Nume: ");              String nume      = scanner.nextLine();
            System.out.print("Prenume: ");           String prenume   = scanner.nextLine();
            System.out.print("CNP (13 caractere): ");String cnp       = scanner.nextLine();
            System.out.print("Adresa: ");            String adresa    = scanner.nextLine();
            System.out.print("Salariu: ");           double salariu   = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Program (HH:mm-HH:mm): "); String program = scanner.nextLine();
            System.out.print("Parafa: ");            String parafa    = scanner.nextLine();

            Specialitate[] vals = Specialitate.values();
            System.out.println("Specialitati disponibile:");
            for (int i = 0; i < vals.length; i++)
                System.out.println("  " + i + ". " + vals[i].getDenumire());
            System.out.print("Alegeti specialitatea (index): ");
            int idx = Integer.parseInt(scanner.nextLine().trim());

            Medic m = new Medic(idAngajat, nume, prenume, cnp, adresa, salariu, program, parafa, vals[idx]);
            medicService.adaugaMedic(m);
            System.out.println("Medic adaugat: " + m.getNumeComplet() + " — " + m.getRol());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  3. Creeaza programare — data/ora primul, medici disponibili dupa

    private void creeazaProgramare() {
        System.out.print("Data si ora dorita (dd.MM.yyyy HH:mm): ");
        String dataOraStr = scanner.nextLine();
        LocalDateTime dataOra;
        try {
            dataOra = LocalDateTime.parse(dataOraStr,
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Format invalid. Folositi: dd.MM.yyyy HH:mm");
            return;
        }

        List<Medic> disponibili = medicService.listeazaDisponibiliLaOra(dataOra);
        if (disponibili.isEmpty()) {
            System.out.println("Nu exista medici disponibili la " + dataOraStr + ".");
            System.out.println("Verificati ca ora se incadreaza in programul de lucru.");
            return;
        }

        System.out.println("\nMedici disponibili la " + dataOraStr + ":");
        for (int i = 0; i < disponibili.size(); i++) {
            Medic m = disponibili.get(i);
            System.out.printf("  %d. %-25s | %-20s | %s | Parafa: %s%n",
                    i, m.getNumeComplet(), m.getSpecialitate().getDenumire(),
                    m.getProgram(), m.getParafa());
        }

        System.out.print("Alegeti medicul (index): ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Index invalid.");
            return;
        }
        if (idx < 0 || idx >= disponibili.size()) {
            System.out.println("Index in afara intervalului.");
            return;
        }
        Medic medic = disponibili.get(idx);

        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        Optional<Pacient> optP = pacientService.cautaDupaCNP(cnp);
        if (optP.isEmpty()) {
            System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
            return;
        }

        System.out.print("Motiv: ");
        String motiv = scanner.nextLine();

        Programare p = new Programare("P" + System.currentTimeMillis(),
                optP.get(), medic, dataOra, motiv);
        try {
            programareService.adaugaProgramare(p);
            System.out.println("Programare creata: " + p);
        } catch (ProgramareConflictException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  4. Anuleaza programare

    private void anuleazaProgramare() {
        System.out.print("ID programare: ");
        String id = scanner.nextLine();
        try {
            programareService.anuleazaProgramare(id);
            System.out.println("Programarea " + id + " a fost anulata.");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  5. Efectueaza consultatie

    private void efectueazaConsultatie() {
        System.out.print("Parafa medic: ");
        String parafa = scanner.nextLine();
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();

        Medic medic;
        try {
            medic = medicService.cautaDupaParafa(parafa);
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
            return;
        }

        Optional<Pacient> optP = pacientService.cautaDupaCNP(cnp);
        if (optP.isEmpty()) {
            System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
            return;
        }

        System.out.print("Simptome: ");
        String simptome = scanner.nextLine();

        Consultatie consultatie;
        try {
            consultatie = medic.consulta(optP.get(), simptome);
            System.out.println("Consultatie efectuata cu succes.");
        } catch (MedicNedisponibilException | PacientNegasitException e) {
            System.out.println("Eroare: " + e.getMessage());
            return;
        }

        System.out.print("Diagnostic: ");
        String diagnostic = scanner.nextLine();
        try {
            consultatie.setDiagnostic(diagnostic);
            System.out.println("Diagnostic setat: " + diagnostic);
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  6. Emite reteta

    private void emiteReteta() {
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        Optional<Pacient> optP = pacientService.cautaDupaCNP(cnp);
        if (optP.isEmpty()) {
            System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
            return;
        }

        Consultatie consultatie = optP.get().getFisaMedicala().getUltimaConsultatie();
        if (consultatie == null) {
            System.out.println("Pacientul nu are consultatii inregistrate.");
            return;
        }

        System.out.println("Introduceti medicamentele (linie goala pentru a termina):");
        List<String> medicamente = new ArrayList<>();
        String med;
        while (!(med = scanner.nextLine()).isBlank()) medicamente.add(med);

        if (medicamente.isEmpty()) {
            System.out.println("Nu au fost introduse medicamente.");
            return;
        }

        try {
            Reteta reteta = consultatie.getMedic()
                    .emiteReteta(consultatie, medicamente.toArray(new String[0]));
            System.out.println("Reteta emisa:");
            reteta.afiseaza();
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  7. Cauta pacient

    private void cautaPacient() {
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        Optional<Pacient> opt = pacientService.cautaDupaCNP(cnp);
        if (opt.isPresent()) opt.get().afiseaza();
        else System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
    }


    //  8. Programarile unui medic

    private void listeazaProgramariMedic() {
        System.out.print("Parafa medic: ");
        String parafa = scanner.nextLine();
        List<Programare> lista = programareService.getProgramariMedic(parafa);
        if (lista.isEmpty()) System.out.println("Medicul cu parafa " + parafa + " nu are programari.");
        else {
            System.out.println("Programari (cronologic) — medic parafa " + parafa + ":");
            for (Programare p : lista) p.afiseaza();
        }
    }


    //  9. Fisa medicala

    private void afiseazaFisaMedicala() {
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        Optional<Pacient> opt = pacientService.cautaDupaCNP(cnp);
        if (opt.isPresent()) opt.get().getFisaMedicala().afiseaza();
        else System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
    }


    //  10. Sterge pacient

    private void stergePacient() {
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        try {
            pacientService.stergePacient(cnp);
            System.out.println("Pacientul cu CNP " + cnp + " a fost sters.");
        } catch (PacientNegasitException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  11. Programarile unui pacient

    private void listeazaProgramariPacient() {
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        if (pacientService.cautaDupaCNP(cnp).isEmpty()) {
            System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
            return;
        }
        List<Programare> lista = programareService.getProgramariPacient(cnp);
        if (lista.isEmpty()) System.out.println("Pacientul nu are programari.");
        else {
            System.out.println("Programari pacient CNP " + cnp + " (cronologic):");
            for (Programare p : lista) p.afiseaza();
        }
    }


    //  12. Ranking medici — general + pe specialitati

    private void listeazaMediciDupaRanking() {
        List<Medic> general = medicService.listeazaDupaRanking();
        if (general.isEmpty()) {
            System.out.println("Nu exista medici in sistem.");
            return;
        }

        String separator = "─".repeat(80);
        String formula   = "Punctaj = programari_total×50 + programari_active×30 + salariu/100";

        // ── RANKING GENERAL ──────────────────────────────────────────────
        System.out.println("\n" + separator);
        System.out.println("  RANKING GENERAL — " + formula);
        System.out.println(separator);
        afiseazaRandingRows(general, 1);

        // ── RANKING PE SPECIALITATI ──────────────────────────────────────
        System.out.println("\n" + separator);
        System.out.println("  RANKING PE SPECIALITATI");
        System.out.println(separator);
        Map<Specialitate, List<Medic>> peSpec = medicService.listeazaDupaRankingPeSpecialitate();
        for (Map.Entry<Specialitate, List<Medic>> entry : peSpec.entrySet()) {
            System.out.println("  ▸ " + entry.getKey().getDenumire().toUpperCase());
            afiseazaRandingRows(entry.getValue(), 1);
        }
        System.out.println(separator);
    }

    private void afiseazaRandingRows(List<Medic> lista, int startLoc) {
        int loc = startLoc;
        for (Medic m : lista) {
            int  punctaj = medicService.calculeazaPunctaj(m);
            long active  = m.getProgramari().stream().filter(Programare::esteActiva).count();
            System.out.printf("  #%-2d %-22s | %-18s | Prog: %2d (act: %d) | Sal: %6.0f RON | Pct: %d%n",
                    loc++,
                    m.getNumeComplet(),
                    m.getSpecialitate().getDenumire(),
                    m.getProgramari().size(), active,
                    m.getSalariu(),
                    punctaj);
        }
    }


    //  13. Pacienti pe tip asigurare

    private void afiseazaPacientiPeAsigurare() {
        Map<String, List<Pacient>> grupuri = pacientService.grupeazaDupaAsigurare();
        if (grupuri.isEmpty()) {
            System.out.println("Nu exista pacienti in sistem.");
            return;
        }
        String separator = "─".repeat(60);
        System.out.println("\n" + separator);
        System.out.println("  PACIENTI GRUPATI PE TIP ASIGURARE");
        System.out.println(separator);
        // Sort keys for consistent display order
        List<String> tipuri = new ArrayList<>(grupuri.keySet());
        Collections.sort(tipuri);
        for (String tip : tipuri) {
            List<Pacient> lista = grupuri.get(tip);
            System.out.printf("  ▸ %-10s  (%d pacienti)%n", tip.toUpperCase(), lista.size());
            for (Pacient p : lista) {
                System.out.printf("      %-25s  CNP: %s  Inscris: %s%n",
                        p.getNumeComplet(),
                        p.getCNP(),
                        p.getDataInscriere().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }
        }
        System.out.println(separator);
        System.out.println("  Total pacienti: " + pacientService.getNrPacienti());
        System.out.println(separator);
    }


    //  14. Adauga analiza la pacient

    private void adaugaAnalizaPacient() {
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine();
        Optional<Pacient> optP = pacientService.cautaDupaCNP(cnp);
        if (optP.isEmpty()) {
            System.out.println("Pacientul cu CNP " + cnp + " nu a fost gasit.");
            return;
        }
        Pacient pacient = optP.get();

        System.out.print("Denumire analiza (ex: Glicemie, Hemoleucograma): ");
        String numAnaliza = scanner.nextLine();
        System.out.print("Rezultat analiza: ");
        String rezultat = scanner.nextLine();

        try {
            pacient.getFisaMedicala().adaugaAnaliza(numAnaliza, rezultat);
            System.out.println("Analiza \"" + numAnaliza + "\" adaugata pentru " + pacient.getNumeComplet() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    //  Date demo
    private void initializeazaDate() {
        try {
            // Medici
            Medic m1 = new Medic("ANG001", "Ionescu",    "Andrei",  "1900101123456",
                    "Str. Libertatii 1, Bucuresti",   8000.0, "08:00-16:00", "IF1234", Specialitate.CARDIOLOGIE);
            Medic m2 = new Medic("ANG002", "Popescu",    "Maria",   "2850315234567",
                    "Str. Florilor 5, Cluj",          7500.0, "09:00-17:00", "CJ5678", Specialitate.PEDIATRIE);
            Medic m3 = new Medic("ANG003", "Constantin", "Elena",   "2780601345678",
                    "Str. Unirii 10, Iasi",           7000.0, "10:00-18:00", "IS9012", Specialitate.NEUROLOGIE);
            Medic m4 = new Medic("ANG004", "Marinescu",  "Radu",    "1920815234567",
                    "Str. Victoriei 22, Timisoara",   6500.0, "08:00-14:00", "TM3456", Specialitate.DERMATOLOGIE);
            medicService.adaugaMedic(m1);
            medicService.adaugaMedic(m2);
            medicService.adaugaMedic(m3);
            medicService.adaugaMedic(m4);

            // Pacienti (3 CNAS, 2 privat)
            Pacient p1 = new Pacient(new CodPacient("PAC-0001"), "Dumitrescu", "Ion",
                    "2900215345678", "Str. Lalelelor 3, Bucuresti",    "CNAS");
            Pacient p2 = new Pacient(new CodPacient("PAC-0002"), "Gheorghe",   "Ana",
                    "2850512456789", "Str. Trandafirilor 7, Cluj",     "privat");
            Pacient p3 = new Pacient(new CodPacient("PAC-0003"), "Vasilescu",  "Mihai",
                    "1880321567890", "Str. Rozelor 15, Iasi",          "CNAS");
            Pacient p4 = new Pacient(new CodPacient("PAC-0004"), "Stancu",     "Lidia",
                    "2910704678901", "Str. Eminescu 8, Timisoara",     "privat");
            Pacient p5 = new Pacient(new CodPacient("PAC-0005"), "Munteanu",   "Vlad",
                    "1950630789012", "Str. Campului 2, Brasov",        "CNAS");
            pacientService.adaugaPacient(p1);
            pacientService.adaugaPacient(p2);
            pacientService.adaugaPacient(p3);
            pacientService.adaugaPacient(p4);
            pacientService.adaugaPacient(p5);

            // Programari
            programareService.adaugaProgramare(new Programare("P001", p1, m1,
                    LocalDateTime.of(2026, 5, 10, 10, 0), "Dureri toracice"));
            programareService.adaugaProgramare(new Programare("P002", p2, m2,
                    LocalDateTime.of(2026, 5, 11, 11, 0), "Control periodic copil"));
            programareService.adaugaProgramare(new Programare("P003", p3, m3,
                    LocalDateTime.of(2026, 5, 12, 10, 0), "Migrene frecvente"));
            programareService.adaugaProgramare(new Programare("P004", p4, m4,
                    LocalDateTime.of(2026, 5, 13, 13, 0), "Iritatie piele"));
            programareService.adaugaProgramare(new Programare("P005", p5, m1,
                    LocalDateTime.of(2026, 5, 10, 14, 0), "Palpitati si ameteli"));
            programareService.adaugaProgramare(new Programare("P006", p1, m3,
                    LocalDateTime.of(2026, 5, 14, 11, 0), "Amorteli membre"));
            programareService.adaugaProgramare(new Programare("P007", p2, m1,
                    LocalDateTime.of(2026, 5, 15,  9, 0), "Tensiune arteriala"));

            // Consultatii + retete
            Consultatie c1 = m1.consulta(p1, "Dureri de cap, ameteala, palpitati");
            c1.setDiagnostic("Hipertensiune arteriala gr. I");
            m1.emiteReteta(c1, new String[]{"Metoprolol 50mg", "Aspirina 75mg"});

            Consultatie c2 = m2.consulta(p2, "Tuse persistenta, febra 38.5, secretii nazale");
            c2.setDiagnostic("Infectie respiratorie acuta");
            m2.emiteReteta(c2, new String[]{"Augmentin 1g", "Paracetamol 500mg", "Sirop Tussin"});

            Consultatie c3 = m3.consulta(p3, "Dureri de cap severe, greata, sensibilitate la lumina");
            c3.setDiagnostic("Migrena cronica fara aura");
            m3.emiteReteta(c3, new String[]{"Sumatriptan 50mg", "Ibuprofen 400mg"});

            Consultatie c4 = m4.consulta(p4, "Eruptii cutanate, mancarimi, roseata");
            c4.setDiagnostic("Dermatita de contact");

            // Analize
            p1.getFisaMedicala().adaugaAnaliza("Glicemie",         "95 mg/dL — Normal");
            p1.getFisaMedicala().adaugaAnaliza("Colesterol total",  "210 mg/dL — Limita");
            p1.getFisaMedicala().adaugaAnaliza("Trigliceride",      "155 mg/dL — Normal");
            p2.getFisaMedicala().adaugaAnaliza("Hemoleucograma",    "Leucocite: 12.000/μL — Crescut");
            p3.getFisaMedicala().adaugaAnaliza("RMN cerebral",      "Fara leziuni structurale");
            p3.getFisaMedicala().adaugaAnaliza("EEG",               "Activitate normala");
            p4.getFisaMedicala().adaugaAnaliza("Teste alergologice","Pozitiv nichel, latex");
            p5.getFisaMedicala().adaugaAnaliza("ECG",               "Ritm sinusal normal, FC 72 bpm");

            System.out.printf("Date demo initializate — %d medici, %d pacienti, %d programari.%n",
                    medicService.listeazaToti().size(),
                    pacientService.getNrPacienti(),
                    programareService.listeazaToate().size());
        } catch (Exception e) {
            System.out.println("Eroare la initializarea datelor demo: " + e.getMessage());
        }
    }
}