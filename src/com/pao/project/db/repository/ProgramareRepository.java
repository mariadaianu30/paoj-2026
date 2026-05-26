package com.pao.project.db.repository;

import com.pao.project.db.DatabaseConnection;
import com.pao.project.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgramareRepository implements Repository<Programare, String> {

    private static ProgramareRepository instance;
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    private ProgramareRepository() {}

    public static synchronized ProgramareRepository getInstance() {
        if (instance == null) instance = new ProgramareRepository();
        return instance;
    }

    @Override
    public Programare save(Programare p) {
        String sqlPac    = "SELECT id FROM pacienti WHERE cnp = ?";
        String sqlMed    = "SELECT id FROM medici WHERE parafa = ?";
        String sqlInsert = "INSERT INTO programari (id_programare, id_pacient, id_medic, data_ora, motiv, status) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = db.getConnection();
            int idPacient = lookupInt(conn, sqlPac, p.getPacient().getCNP(), "Pacient inexistent in DB");
            int idMedic   = lookupInt(conn, sqlMed, p.getMedic().getParafa(), "Medic inexistent in DB");
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setString(1, p.getIdProgramare());
                ps.setInt(2, idPacient);
                ps.setInt(3, idMedic);
                ps.setTimestamp(4, Timestamp.valueOf(p.getDataOra()));
                ps.setString(5, p.getMotiv());
                ps.setString(6, p.getStatus().name());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea programarii: " + e.getMessage(), e);
        }
        return p;
    }

    @Override
    public Optional<Programare> findById(String idProgramare) {
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(joinSql() + " WHERE pr.id_programare = ?")) {
                ps.setString(1, idProgramare);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea programarii: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // JOIN 3 — programari JOIN pacienti JOIN medici: toate programarile cu detalii complete
    @Override
    public List<Programare> findAll() {
        List<Programare> lista = new ArrayList<>();
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(joinSql() + " ORDER BY pr.data_ora");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea programarilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Programare update(Programare p) {
        String sql = "UPDATE programari SET data_ora=?, status=? WHERE id_programare=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setTimestamp(1, Timestamp.valueOf(p.getDataOra()));
                ps.setString(2, p.getStatus().name());
                ps.setString(3, p.getIdProgramare());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea programarii: " + e.getMessage(), e);
        }
        return p;
    }

    @Override
    public void delete(String idProgramare) {
        String sql = "DELETE FROM programari WHERE id_programare=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idProgramare);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea programarii: " + e.getMessage(), e);
        }
    }

    private String joinSql() {
        return "SELECT pr.id_programare, pr.data_ora, pr.motiv, pr.status, " +
               "pac.cod_pacient, pac.cnp AS pac_cnp, pac.nume AS pac_nume, pac.prenume AS pac_prenume, " +
               "pac.adresa AS pac_adresa, pac.data_inscriere, pac.tip_asigurare, " +
               "m.parafa, m.cnp AS med_cnp, m.nume AS med_nume, m.prenume AS med_prenume, " +
               "m.adresa AS med_adresa, m.id_angajat, m.salariu, m.program, m.specialitate " +
               "FROM programari pr " +
               "JOIN pacienti pac ON pr.id_pacient = pac.id " +
               "JOIN medici m ON pr.id_medic = m.id";
    }

    private Programare mapRow(ResultSet rs) throws SQLException {
        java.time.LocalDate dataInscriere = rs.getDate("data_inscriere").toLocalDate();
        Pacient pacient = new Pacient(
                new CodPacient(rs.getString("cod_pacient"), dataInscriere),
                rs.getString("pac_nume"), rs.getString("pac_prenume"),
                rs.getString("pac_cnp"), rs.getString("pac_adresa"),
                rs.getString("tip_asigurare"));
        pacient.setDataInscriere(dataInscriere);

        Medic medic = new Medic(
                rs.getString("id_angajat"),
                rs.getString("med_nume"), rs.getString("med_prenume"),
                rs.getString("med_cnp"), rs.getString("med_adresa"),
                rs.getDouble("salariu"), rs.getString("program"),
                rs.getString("parafa"),
                Specialitate.valueOf(rs.getString("specialitate")));

        Programare pr = new Programare(
                rs.getString("id_programare"), pacient, medic,
                rs.getTimestamp("data_ora").toLocalDateTime(),
                rs.getString("motiv"));
        pr.setStatus(StatusProgramare.valueOf(rs.getString("status")));
        return pr;
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
