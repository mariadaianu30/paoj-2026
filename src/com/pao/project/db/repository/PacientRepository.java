package com.pao.project.db.repository;

import com.pao.project.audit.AuditService;
import com.pao.project.db.DatabaseConnection;
import com.pao.project.model.CodPacient;
import com.pao.project.model.Pacient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacientRepository implements Repository<Pacient, String> {

    private static PacientRepository instance;
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    private PacientRepository() {}

    public static synchronized PacientRepository getInstance() {
        if (instance == null) instance = new PacientRepository();
        return instance;
    }

    @Override
    public Pacient save(Pacient p) {
        String sql = "INSERT INTO pacienti (cod_pacient, cnp, nume, prenume, adresa, data_inscriere, tip_asigurare) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getCodPacient().getCod());
                ps.setString(2, p.getCNP());
                ps.setString(3, p.getNume());
                ps.setString(4, p.getPrenume());
                ps.setString(5, p.getAdresa());
                ps.setDate(6, Date.valueOf(p.getDataInscriere()));
                ps.setString(7, p.getTipAsigurare());
                ps.executeUpdate();
            }
            AuditService.getInstance().log("SAVE_PACIENT");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea pacientului: " + e.getMessage(), e);
        }
        return p;
    }

    @Override
    public Optional<Pacient> findById(String cnp) {
        String sql = "SELECT * FROM pacienti WHERE cnp = ?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cnp);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea pacientului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pacient> findAll() {
        List<Pacient> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacienti ORDER BY data_inscriere";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea pacientilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Pacient update(Pacient p) {
        String sql = "UPDATE pacienti SET nume=?, prenume=?, adresa=?, tip_asigurare=? WHERE cnp=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getNume());
                ps.setString(2, p.getPrenume());
                ps.setString(3, p.getAdresa());
                ps.setString(4, p.getTipAsigurare());
                ps.setString(5, p.getCNP());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea pacientului: " + e.getMessage(), e);
        }
        return p;
    }

    @Override
    public void delete(String cnp) {
        String sql = "DELETE FROM pacienti WHERE cnp=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cnp);
                ps.executeUpdate();
            }
            AuditService.getInstance().log("DELETE_PACIENT");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea pacientului: " + e.getMessage(), e);
        }
    }

    // JOIN 1 — pacienti LEFT JOIN programari: fiecare pacient cu numarul sau de programari
    public List<String> findPacientiCuNrProgramari() {
        List<String> rezultate = new ArrayList<>();
        String sql = "SELECT p.prenume, p.nume, p.cnp, COUNT(pr.id) AS nr_programari " +
                     "FROM pacienti p " +
                     "LEFT JOIN programari pr ON p.id = pr.id_pacient " +
                     "GROUP BY p.id, p.prenume, p.nume, p.cnp " +
                     "ORDER BY nr_programari DESC";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultate.add(String.format("%s %s (CNP: %s) — %d programari",
                            rs.getString("prenume"), rs.getString("nume"),
                            rs.getString("cnp"), rs.getInt("nr_programari")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare JOIN pacienti-programari: " + e.getMessage(), e);
        }
        return rezultate;
    }

    private Pacient mapRow(ResultSet rs) throws SQLException {
        java.time.LocalDate dataInscriere = rs.getDate("data_inscriere").toLocalDate();
        Pacient p = new Pacient(
                new CodPacient(rs.getString("cod_pacient"), dataInscriere),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("cnp"),
                rs.getString("adresa"),
                rs.getString("tip_asigurare"));
        p.setDataInscriere(dataInscriere);
        return p;
    }
}
