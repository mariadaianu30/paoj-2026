package com.pao.project.db.repository;

import com.pao.project.db.DatabaseConnection;
import com.pao.project.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultatieRepository implements Repository<Consultatie, Integer> {

    private static ConsultatieRepository instance;
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    private ConsultatieRepository() {}

    public static synchronized ConsultatieRepository getInstance() {
        if (instance == null) instance = new ConsultatieRepository();
        return instance;
    }

    @Override
    public Consultatie save(Consultatie c) {
        try {
            Connection conn = db.getConnection();
            insertConsultatie(conn, c);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea consultatiei: " + e.getMessage(), e);
        }
        return c;
    }

    /**
     * Tranzactie JDBC explicita: salveaza consultatia SI marcheaza programarea
     * asociata ca FINALIZAT — ambele operatii au loc atomic.
     * Daca una esueaza, rollback complet.
     */
    public Consultatie salveazaConsultatieSimarcaProgramarea(Consultatie c, String idProgramare) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            insertConsultatie(conn, c);

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE programari SET status = 'FINALIZAT' WHERE id_programare = ?")) {
                ps.setString(1, idProgramare);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Tranzactie esuata (consultatie + programare): " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        }
        return c;
    }

    @Override
    public Optional<Consultatie> findById(Integer id) {
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(joinSql() + " WHERE c.id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea consultatiei: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // JOIN 4 — consultatii JOIN medici JOIN pacienti: toate consultatiile cu detalii complete
    @Override
    public List<Consultatie> findAll() {
        List<Consultatie> lista = new ArrayList<>();
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(joinSql() + " ORDER BY c.data_ora");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea consultatiilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Consultatie update(Consultatie c) {
        String sqlMed = "SELECT id FROM medici WHERE parafa = ?";
        String sqlPac = "SELECT id FROM pacienti WHERE cnp = ?";
        String sqlUpd = "UPDATE consultatii SET diagnostic=? WHERE id_medic=? AND id_pacient=? AND data_ora=?";
        try {
            Connection conn = db.getConnection();
            int idMedic   = lookupInt(conn, sqlMed, c.getMedic().getParafa(), "Medic inexistent");
            int idPacient = lookupInt(conn, sqlPac, c.getPacient().getCNP(), "Pacient inexistent");
            try (PreparedStatement ps = conn.prepareStatement(sqlUpd)) {
                ps.setString(1, c.getDiagnostic());
                ps.setInt(2, idMedic);
                ps.setInt(3, idPacient);
                ps.setTimestamp(4, Timestamp.valueOf(c.getDataOra()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea consultatiei: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM consultatii WHERE id=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea consultatiei: " + e.getMessage(), e);
        }
    }

    private void insertConsultatie(Connection conn, Consultatie c) throws SQLException {
        String sqlMed    = "SELECT id FROM medici WHERE parafa = ?";
        String sqlPac    = "SELECT id FROM pacienti WHERE cnp = ?";
        String sqlInsert = "INSERT INTO consultatii (id_medic, id_pacient, data_ora, simptome, diagnostic) " +
                           "VALUES (?, ?, ?, ?, ?)";
        int idMedic   = lookupInt(conn, sqlMed, c.getMedic().getParafa(), "Medic inexistent in DB");
        int idPacient = lookupInt(conn, sqlPac, c.getPacient().getCNP(), "Pacient inexistent in DB");
        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, idMedic);
            ps.setInt(2, idPacient);
            ps.setTimestamp(3, Timestamp.valueOf(c.getDataOra()));
            ps.setString(4, c.getSimptome());
            ps.setString(5, c.getDiagnostic());
            ps.executeUpdate();
        }
    }

    private String joinSql() {
        return "SELECT c.id, c.data_ora, c.simptome, c.diagnostic, " +
               "m.parafa, m.cnp AS med_cnp, m.nume AS med_nume, m.prenume AS med_prenume, " +
               "m.adresa AS med_adresa, m.id_angajat, m.salariu, m.program, m.specialitate, " +
               "p.cod_pacient, p.cnp AS pac_cnp, p.nume AS pac_nume, p.prenume AS pac_prenume, " +
               "p.adresa AS pac_adresa, p.data_inscriere, p.tip_asigurare " +
               "FROM consultatii c " +
               "JOIN medici m ON c.id_medic = m.id " +
               "JOIN pacienti p ON c.id_pacient = p.id";
    }

    private Consultatie mapRow(ResultSet rs) throws SQLException {
        java.time.LocalDate dataInscriere = rs.getDate("data_inscriere").toLocalDate();
        Medic medic = new Medic(
                rs.getString("id_angajat"),
                rs.getString("med_nume"), rs.getString("med_prenume"),
                rs.getString("med_cnp"), rs.getString("med_adresa"),
                rs.getDouble("salariu"), rs.getString("program"),
                rs.getString("parafa"),
                Specialitate.valueOf(rs.getString("specialitate")));

        Pacient pacient = new Pacient(
                new CodPacient(rs.getString("cod_pacient"), dataInscriere),
                rs.getString("pac_nume"), rs.getString("pac_prenume"),
                rs.getString("pac_cnp"), rs.getString("pac_adresa"),
                rs.getString("tip_asigurare"));
        pacient.setDataInscriere(dataInscriere);

        Consultatie c = new Consultatie(medic, pacient, rs.getString("simptome"),
                rs.getTimestamp("data_ora").toLocalDateTime());
        String diag = rs.getString("diagnostic");
        if (diag != null && !diag.isBlank()) c.setDiagnostic(diag);
        return c;
    }

    private int lookupInt(Connection conn, String sql, String param, String errMsg) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException(errMsg + ": " + param);
                return rs.getInt(1);
            }
        }
    }
}
