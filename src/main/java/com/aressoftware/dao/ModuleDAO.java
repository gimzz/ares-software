package com.aressoftware.dao;

import com.aressoftware.model.security.Module;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ModuleDAO
 *
 * DAO encargado de manejar las operaciones sobre la tabla:
 * seguridad.modulos
 *
 * Los módulos representan las áreas del sistema:
 * Ejemplo: 'Ventas', 'Inventario', 'Contabilidad'.
 */
public class ModuleDAO extends BaseDAO {

    /**
     * Convierte una fila del ResultSet en un objeto Module.
     */
    private Module mapToModule(ResultSet rs) throws SQLException {
        return new Module(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("activo")
        );
    }

    public Module findById(int id) {
        String sql = "SELECT * FROM seguridad.modulos WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToModule(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Module findByName(String name) {
        String sql = "SELECT * FROM seguridad.modulos WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToModule(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Module> findAll() {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM seguridad.modulos ORDER BY id ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                modules.add(mapToModule(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return modules;
    }

    public boolean create(Module module) {
        String sql = """
                INSERT INTO seguridad.modulos (nombre, descripcion, activo)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, module.getNombre());
            stmt.setString(2, module.getDescripcion());
            stmt.setBoolean(3, module.isActivo());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Module module) {
        String sql = """
                UPDATE seguridad.modulos
                SET nombre = ?, descripcion = ?, activo = ?
                WHERE id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, module.getNombre());
            stmt.setString(2, module.getDescripcion());
            stmt.setBoolean(3, module.isActivo());
            stmt.setInt(4, module.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeStatus(int id, boolean activo) {
        String sql = "UPDATE seguridad.modulos SET activo = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, activo);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
