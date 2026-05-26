package com.pao.project.db.repository;

import com.pao.project.audit.AuditService;
import com.pao.project.db.DatabaseConnection;
import com.pao.project.model.Medic;
import com.pao.project.model.Specialitate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicRepository implements Repository<Medic, String> {

    private static MedicRepository instance;
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    private MedicRepository() {}

    public static synchronized MedicRepository getInstance() {
        if (instance == null) instance = new MedicRepository();
        return instance;
    }

    @Override
    public Medic save(Medic m) {
        String sql = "INSERT INTO medici (parafa, cnp, nume, prenume, adresa, id_angajat, salariu, program, specialitate, disponibil) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, m.getParafa());
                ps.setString(2, m.getCNP());
                ps.setString(3, m.getNume());
                ps.setString(4, m.getPrenume());
                ps.setString(5, m.getAdresa());
                ps.setString(6, m.getIdAngajat());
                ps.setDouble(7, m.getSalariu());
                ps.setString(8, m.getProgram());
                ps.setString(9, m.getSpecialitate().name());
                ps.setBoolean(10, m.esteDisponibil());
                ps.executeUpdate();
            }
            AuditService.getInstance().log("SAVE_MEDIC");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea medicului: " + e.getMessage(), e);
        }
        return m;
    }

    @Override
    public Optional<Medic> findById(String parafa) {
        String sql = "SELECT * FROM medici WHERE parafa = ?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, parafa);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea medicului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Medic> findAll() {
        List<Medic> lista = new ArrayList<>();
        String sql = "SELECT * FROM medici ORDER BY nume, prenume";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea medicilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Medic update(Medic m) {
        String sql = "UPDATE medici SET salariu=?, program=?, specialitate=?, disponibil=? WHERE parafa=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, m.getSalariu());
                ps.setString(2, m.getProgram());
                ps.setString(3, m.getSpecialitate().name());
                ps.setBoolean(4, m.esteDisponibil());
                ps.setString(5, m.getParafa());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea medicului: " + e.getMessage(), e);
        }
        return m;
    }

    @Override
    public void delete(String parafa) {
        String sql = "DELETE FROM medici WHERE parafa=?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, parafa);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea medicului: " + e.getMessage(), e);
        }
    }

    // JOIN 2 — medici LEFT JOIN programari: numarul de programari per medic, grupat pe specialitate
    public List<String> findMediciCuNrProgramariPeSpecialitate() {
        List<String> rezultate = new ArrayList<>();
        String sql = "SELECT m.prenume, m.nume, m.parafa, m.specialitate, COUNT(p.id) AS nr_programari " +
                     "FROM medici m " +
                     "LEFT JOIN programari p ON m.id = p.id_medic " +
                     "GROUP BY m.id, m.prenume, m.nume, m.parafa, m.specialitate " +
                     "ORDER BY m.specialitate, nr_programari DESC";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultate.add(String.format("[%-20s] %s %s (parafa: %s) — %d programari",
                            rs.getString("specialitate"),
                            rs.getString("prenume"), rs.getString("nume"),
                            rs.getString("parafa"), rs.getInt("nr_programari")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare JOIN medici-programari: " + e.getMessage(), e);
        }
        return rezultate;
    }

    private Medic mapRow(ResultSet rs) throws SQLException {
        return new Medic(
                rs.getString("id_angajat"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("cnp"),
                rs.getString("adresa"),
                rs.getDouble("salariu"),
                rs.getString("program"),
                rs.getString("parafa"),
                Specialitate.valueOf(rs.getString("specialitate")));
    }
}
