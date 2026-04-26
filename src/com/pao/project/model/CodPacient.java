package com.pao.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class CodPacient {
    private final String cod;
    private final LocalDate dataEmiterii;

    public CodPacient(String cod) {
        if (!cod.matches("PAC-\\d{4}")) {
            throw new IllegalArgumentException("Cod pacient invalid. Format asteptat: PAC-XXXX");
        }
        this.cod = cod;
        this.dataEmiterii = LocalDate.now();
    }

    public CodPacient(String cod, LocalDate dataEmiterii) {
        if (!cod.matches("PAC-\\d{4}")) {
            throw new IllegalArgumentException("Cod pacient invalid. Format asteptat: PAC-XXXX");
        }
        if (dataEmiterii.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data emiterii nu poate fi in viitor.");
        }
        this.cod = cod;
        this.dataEmiterii = dataEmiterii;
    }

    public String getCod() { return cod; }
    public LocalDate getDataEmiterii() { return dataEmiterii; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodPacient other)) return false;
        return cod.equals(other.cod);
    }

    @Override
    public int hashCode() { return cod.hashCode(); }

    @Override
    public String toString() {
        return cod + " (emis: " + dataEmiterii.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")";
    }
}