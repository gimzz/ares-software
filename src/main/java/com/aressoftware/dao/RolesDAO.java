package com.aressoftware.dao;

import com.aressoftware.config.DatabaseConfig;
import com.aressoftware.model.security.Roles;
import com.aressoftware.model.security.User;
import com.aressoftware.util.PasswordUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolesDAO extends BaseDAO {

    private Roles mapToRoles(ResultSet rs) throws SQLException {
        return new Roles(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion")

        );
    }

    public Roles findByNombre(String nombre){
         String sql = "SELECT * FROM seguridad.roles WHERE nombre = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToRoles(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Roles findById(int id){
        String sql = "SELECT * FROM seguridad.roles WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToRoles(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Roles> findAll(){
          List<Roles> roles = new ArrayList<>();
        String sql = "SELECT * FROM seguridad.roles ORDER BY id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                roles.add(mapToRoles(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }

    public boolean create(Roles rol) {
    String sql = "INSERT INTO seguridad.roles (nombre, descripcion) VALUES (?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, rol.getNombre());
        stmt.setString(2, rol.getDescripcion());
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean update(Roles rol) {
        String sql = "UPDATE seguridad.roles SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());
            stmt.setInt(3, rol.getId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) { 
        String sql = "DELETE FROM seguridad.roles WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

public List<Roles> search(String filtro) {
    List<Roles> lista = new ArrayList<>();
    String sql = "SELECT * FROM seguridad.roles WHERE nombre ILIKE ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, "%" + filtro + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) lista.add(mapToRoles(rs));

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}


}
