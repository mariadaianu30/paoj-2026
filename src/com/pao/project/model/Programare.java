package com.pao.project.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Programare implements Comparable<Programare> {
    private final String idProgramare;
    private final Pacient pacient;
    private final Medic medic;
    private LocalDateTime dataOra;
    private final String motiv;
    private StatusProgramare status;

    public Programare(String id, Pacient pacient, Medic medic, LocalDateTime dataOra, String motiv) {
        this.idProgramare = id;
        this.pacient = pacient;
        this.medic = medic;
        this.dataOra = dataOra;
        this.motiv = motiv;
        this.status = StatusProgramare.PROGRAMAT;
    }

    @Override
    public int compareTo(Programare alt) { return this.dataOra.compareTo(alt.dataOra); }

    public boolean esteActiva() {
        return status == StatusProgramare.PROGRAMAT || status == StatusProgramare.CONFIRMAT;
    }

    public void anuleaza() { this.status = StatusProgramare.ANULAT; }
    public void finalizeaza() { this.status = StatusProgramare.FINALIZAT; }
    public void confirma() { this.status = StatusProgramare.CONFIRMAT; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Programare other)) return false;
        return idProgramare.equals(other.idProgramare);
    }

    @Override
    public int hashCode() { return idProgramare.hashCode(); }

    @Override
    public String toString() {
        String data = dataOra.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return "Programare{id='" + idProgramare
                + "', medic='" + medic.getNumeComplet()
                + "', pacient='" + pacient.getNumeComplet()
                + "', data='" + data
                + "', status=" + status + "}";
    }

    public void afiseaza() { System.out.println(toString()); }

    public String getIdProgramare() { return idProgramare; }
    public Pacient getPacient() { return pacient; }
    public Medic getMedic() { return medic; }
    public LocalDateTime getDataOra() { return dataOra; }
    public String getMotiv() { return motiv; }
    public StatusProgramare getStatus() { return status; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
    public void setStatus(StatusProgramare status) { this.status = status; }
}