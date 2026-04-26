# Cabinet Medical — Documentatie Proiect (Etapa I)

**Tema:** Cabinet medical — pacienți, medici, programări, consultații
**Pachet:** `com.pao.project`
**Student:** Maria Daianu

---

## 1.1 Actiuni / interogari posibile in sistem

| # | Actiune |
|---|---------|
| 1 | **Inregistreaza un pacient nou** — se genereaza automat un cod unic (`PAC-XXXX`), se alege tipul de asigurare (CNAS / privat) |
| 2 | **Adauga un medic** — specialitate, parafa, program de lucru (ex: `08:00–16:00`), salariu |
| 3 | **Creeaza o programare** — se introduce data si ora dorita; sistemul afiseaza doar medicii disponibili in acel interval (program de lucru + fara alt pacient in ora respectiva); utilizatorul alege din lista |
| 4 | **Anuleaza o programare** — dupa ID, statutul trece la `ANULAT` atat in serviciu cat si la medic |
| 5 | **Efectueaza o consultatie medicala** — inregistreaza simptomele si diagnosticul; consultatia este adaugata automat in fisa medicala a pacientului |
| 6 | **Emite o reteta** — pe baza ultimei consultatii a pacientului, cu lista de medicamente; valabilitate 30 de zile |
| 7 | **Cauta un pacient dupa CNP** — afiseaza datele pacientului si fisa medicala completa |
| 8 | **Listeaza programarile unui medic** — in ordine cronologica (garantat de `TreeSet<Programare>` cu `Comparable`) |
| 9 | **Afiseaza fisa medicala a unui pacient** — consultatii cu diagnostice, retete si analize de laborator |
| 10 | **Sterge un pacient din sistem** — arunca `PacientNegasitException` daca CNP-ul nu exista |
| 11 | **Listeaza toate programarile unui pacient** — in ordine cronologica |
| 12 | **Ranking medici** — clasament general si pe specialitati dupa un punctaj compus: `programari_total × 50 + programari_active × 30 + salariu / 100`; sortat cu `Comparator` |
| 13 | **Grupeaza pacientii dupa tipul de asigurare** — CNAS vs. privat, cu numar si detalii individuale |
| 14 | **Adauga analize de laborator** — denumire analiza + rezultat, stocate in `Map<String, String>` din fisa medicala |

---

## 1.2 Tipuri de obiecte din domeniu

| Clasa / Enum | Descriere |
|---|---|
| `Medic` | Specialist medical cu specialitate, parafa, program de lucru si lista proprie de programari; implementeaza `Consultabil`, `Programabil`, `Comparable<Medic>` |
| `Pacient` | Persoana cu cod unic imutabil, tip asigurare si fisa medicala proprie; implementeaza `Comparable<Pacient>` (dupa data inscrierii) |
| `Programare` | Rezervare la un medic la o data/ora specifica, cu durata de **1 ora**; implementeaza `Comparable<Programare>` pentru sortare cronologica in `TreeSet` |
| `Consultatie` | Eveniment medical cu simptome, diagnostic si reteta optionala; `equals/hashCode` dupa tripletul medic + pacient + dataOra |
| `FisaMedicala` | Agregat al consultatiilor (`List<Consultatie>`) si analizelor de laborator (`Map<String, String>`) unui pacient |
| `Reteta` | Prescriptie medicala cu lista de medicamente (`List<String>`) si valabilitate 30 de zile de la emitere |
| `CodPacient` | Clasa **imutabila** (`final`, fara setteri) cu format validat regex `PAC-\d{4}` si data emiterii |
| `Asistent` | Personal medical auxiliar cu grad (I/II) si certificat; demonstreaza polimorfism fata de `Medic` prin `descrieActivitate()` |
| `Specialitate` | Enum cu 6 specialitati (Cardiologie, Pediatrie, Neurologie, Dermatologie, Medicina generala, Ortopedie) |
| `StatusProgramare` | Enum cu starile `PROGRAMAT`, `CONFIRMAT`, `ANULAT`, `FINALIZAT` |

---

## Ierarhie de clase

```
Persoana  (abstract, implementeaza IOperatiiCitireScriere)
├── Angajat  (abstract — adauga idAngajat, salariu, program; declara descrieActivitate())
│   ├── Medic     ← implementeaza Consultabil, Programabil, Comparable<Medic>
│   └── Asistent  ← grad + certificat
└── Pacient  ← implementeaza Comparable<Pacient>
```

## Servicii Singleton

| Serviciu | Colectie interna | Cheie |
|---|---|---|
| `PacientService` | `Map<String, Pacient>` | CNP |
| `MedicService` | `Map<String, Medic>` | parafa |
| `ProgramareService` | `TreeSet<Programare>` | sortare cronologica automata |

## Exceptii custom

| Exceptie | Tip | Cand se arunca |
|---|---|---|
| `PacientNegasitException` | `RuntimeException` | cautare / stergere dupa CNP inexistent |
| `MedicNedisponibilException` | `RuntimeException` | consultatie cu medic marcat ca indisponibil |
| `ProgramareConflictException` | `Exception` (checked) | programare in intervalul orar (1 ora) al unei programari existente |

## Regula de programare

O programare ocupa **exact 1 ora**. Verificarea conflictului foloseste:

```java
Math.abs(Duration.between(existing.getDataOra(), new.getDataOra()).toMinutes()) < 60
```

Aceasta regula este aplicata atat la crearea programarii (`ProgramareService`, `Medic.areConflictOrar`)
cat si la filtrarea medicilor disponibili (`MedicService.listeazaDisponibiliLaOra`).

## Pornire aplicatie

```
Main.main() → Meniu.getInstance().ruleaza()
```

La pornire se initializeaza automat date demo:
4 medici, 5 pacienti, 7 programari, 4 consultatii (3 cu retete), 8 analize de laborator.