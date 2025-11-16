package com.aressoftware.dao;

import com.aressoftware.model.security.User;
import com.aressoftware.util.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO {

    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("usuario"),
                rs.getString("contraseña"),
                rs.getInt("id_rol"),
                rs.getString("estado")
        );
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM seguridad.usuarios WHERE usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findById(int id) {
        String sql = "SELECT * FROM seguridad.usuarios WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll() {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM seguridad.usuarios ORDER BY id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                usuarios.add(mapToUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public boolean create(User user) {
        String sql = "INSERT INTO seguridad.usuarios (nombre, usuario, contraseña, id_rol, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getUsuario());
            stmt.setString(3, hashedPassword); 
            stmt.setInt(4, user.getIdRol());
            stmt.setString(5, user.getEstado());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
public boolean updateUserData(User user) {
    String sql = "UPDATE seguridad.usuarios SET nombre = ?, usuario = ?, id_rol = ?, estado = ? WHERE id = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, user.getNombre());
        stmt.setString(2, user.getUsuario());
        stmt.setInt(3, user.getIdRol());
        stmt.setString(4, user.getEstado());
        stmt.setInt(5, user.getId());

        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public boolean updatePassword(int userId, String newPasswordPlaintext) {
    String sql = "UPDATE seguridad.usuarios SET contraseña = ? WHERE id = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        String hashedPassword = PasswordUtils.hashPassword(newPasswordPlaintext);

        stmt.setString(1, hashedPassword);
        stmt.setInt(2, userId);

        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE seguridad.usuarios SET estado = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkLogin(String username, String plainPassword) {
        User user = findByUsername(username);
        if (user == null) return false;
        String storedHash = user.getPassword();
        return PasswordUtils.checkPassword(plainPassword, storedHash);
    }
}