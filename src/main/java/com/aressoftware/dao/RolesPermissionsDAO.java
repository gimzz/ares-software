package com.aressoftware.dao;

import com.aressoftware.model.security.RolesPermissions;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RolesPermissionsDAO
 *
 * Maneja las operaciones sobre la tabla seguridad.rolespermisos.
 * La tabla almacena qué permisos tiene cada rol sobre cada módulo.
 */
public class RolesPermissionsDAO extends BaseDAO {

    /**
     * Convierte un ResultSet en un objeto RolesPermissions.
     */
    private RolesPermissions mapToRolesPermissions(ResultSet rs) throws SQLException {
        return new RolesPermissions(
                rs.getInt("id"),
                rs.getInt("id_rol"),
                rs.getInt("id_modulo"),
                rs.getString("permiso")
        );
    }

    /**
     * Obtiene todos los permisos asignados a un rol.
     */
    public List<RolesPermissions> findByRole(int roleId) {
        List<RolesPermissions> permisos = new ArrayList<>();

        String sql = """
                SELECT * FROM seguridad.rolespermisos
                WHERE id_rol = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                permisos.add(mapToRolesPermissions(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return permisos;
    }

    /**
     * Obtiene permisos por rol y módulo.
     */
    public List<RolesPermissions> findByRoleAndModule(int roleId, int moduleId) {
        List<RolesPermissions> permisos = new ArrayList<>();

        String sql = """
                SELECT * FROM seguridad.rolespermisos
                WHERE id_rol = ? AND id_modulo = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            stmt.setInt(2, moduleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                permisos.add(mapToRolesPermissions(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return permisos;
    }

    /**
     * Verifica si un rol tiene un permiso específico sobre un módulo.
     */
    public boolean hasPermission(int roleId, int moduleId, String permission) {

        String sql = """
                SELECT 1
                FROM seguridad.rolespermisos
                WHERE id_rol = ? AND id_modulo = ? AND permiso = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            stmt.setInt(2, moduleId);
            stmt.setString(3, permission);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Crea un nuevo permiso para un rol en un módulo.
     */
    public boolean create(RolesPermissions perm) {

        String sql = """
                INSERT INTO seguridad.rolespermisos (id_rol, id_modulo, permiso)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perm.getRoleId());
            stmt.setInt(2, perm.getModuleId());
            stmt.setString(3, perm.getPermission());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un permiso por ID.
     */
    public boolean delete(int id) {

        String sql = "DELETE FROM seguridad.rolespermisos WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
