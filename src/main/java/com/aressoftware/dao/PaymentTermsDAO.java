package com.aressoftware.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aressoftware.model.configuration.PaymentTerms;

import java.sql.Statement;

public class PaymentTermsDAO extends BaseDAO {
    
    /**
     * Convierte una fila del ResultSet en un objeto PaymentTerms.
     */
   private PaymentTerms mapToPaymentTerms(ResultSet rs) throws SQLException {
        return new PaymentTerms(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"));
    }

    public PaymentTerms findById(int id) {
        String sql = "SELECT * FROM configuracion.CondicionesPago WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapToPaymentTerms(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PaymentTerms> findAll(){
        List<PaymentTerms> paymentTerms = new ArrayList<>();
        String sql = "SELECT * FROM configuracion.CondicionesPago ORDER BY id ASC";
        try (Connection conn = getConnection();
                Statement stm = conn.createStatement()) {
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                paymentTerms.add(mapToPaymentTerms(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentTerms;
    }

    public boolean create(PaymentTerms paymentTerms){
        String sql = """
                INSERT INTO configuracion.CondicionesPago (nombre, descripcion)
                VALUES (?, ?)
                """;
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, paymentTerms.getNombre());
            stm.setString(2, paymentTerms.getDescripcion());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PaymentTerms paymentTerms){
        String sql = """
                UPDATE configuracion.CondicionesPago set nombre = ?, descripcion = ? WHERE id = ?
                """;
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, paymentTerms.getNombre());
            stm.setString(2, paymentTerms.getDescripcion());
            stm.setInt(3, paymentTerms.getId());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM configuracion.CondicionesPago WHERE id =?";
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return false;
    }

    public PaymentTerms SearchByName(String name){
        String sql = "SELECT * FROM configuracion.CondicionesPago WHERE nombre = ?";
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapToPaymentTerms(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
