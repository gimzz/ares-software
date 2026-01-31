package com.aressoftware.dao;
import java.util.List;
import com.aressoftware.model.configuration.ClientType;
import java.sql.*;
import java.util.ArrayList;

public class ClientTypeDAO extends BaseDAO {

    private ClientType mapToClientType(ResultSet rs) throws SQLException {
        return new ClientType(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"));
    }

    public ClientType findById(int id) {
        String sql = "SELECT * FROM configuracion.TiposCliente WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapToClientType(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ClientType> findAll() {
        List<ClientType> clientType = new ArrayList<>();
        String sql = "SELECT * FROM configuracion.TiposCliente ORDER BY id ASC";
        try (Connection conn = getConnection();
                Statement stm = conn.createStatement()) {
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                clientType.add(mapToClientType(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientType;
    }

    public boolean create(ClientType clientType) {
        String sql = """
                INSERT INTO configuracion.TiposCliente (nombre, descripcion)
                VALUES (?, ?)
                """;
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, clientType.getNombre());
            stm.setString(2, clientType.getDescripcion());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean update(ClientType clientType) {
        String sql = """
                UPDATE configuracion.TiposCliente set nombre = ?, descripcion = ? WHERE id = ?
                """;
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, clientType.getNombre());
            stm.setString(2, clientType.getDescripcion());
            stm.setInt(3, clientType.getId());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }

    public boolean delete(int id) {
        String sql = "DELETE FROM configuracion.TiposCliente WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public ClientType SearchByName(String name) {
        String sql = "SELECT * FROM configuracion.TiposCliente WHERE nombre = ?";
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapToClientType(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
