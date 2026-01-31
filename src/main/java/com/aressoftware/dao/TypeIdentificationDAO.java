package com.aressoftware.dao;
import java.util.List;
import com.aressoftware.model.configuration.TypeIdentification;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

public class TypeIdentificationDAO extends BaseDAO {

    private TypeIdentification mapToTypeIdentification(ResultSet rs) throws SQLException{
        return new TypeIdentification(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"));
    }

    public TypeIdentification findById(int id){
        String sql = "SELECT * FROM configuracion.TiposIdentificacion WHERE id = ?";
        try(Connection conn = getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return mapToTypeIdentification(rs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<TypeIdentification> findAll(){
        List<TypeIdentification> typeIdentification = new ArrayList<>();
        String sql = "SELECT * FROM configuracion.TiposIdentificacion ORDER BY id ASC";
        try(Connection conn = getConnection();
    PreparedStatement stm = conn.prepareStatement(sql)){
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                typeIdentification.add(mapToTypeIdentification(rs));
            }
        }catch(Exception e){
            e.printStackTrace();
    }        
        return typeIdentification;
    }

    public boolean create(TypeIdentification typeIdentification){
        String sql = """
                INSERT INTO configuracion.TiposIdentificacion (nombre, descripcion)
                VALUES (?, ?)
                """;
        try(Connection conn = getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, typeIdentification.getNombre());
            stm.setString(2, typeIdentification.getDescripcion());
            return stm.executeUpdate() > 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(TypeIdentification typeIdentification){
        String sql = """
                UPDATE configuracion.TiposIdentificacion set nombre = ?, descripcion = ? WHERE id = ?
                """;
        try(Connection conn = getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, typeIdentification.getNombre());
            stm.setString(2, typeIdentification.getDescripcion());
            stm.setInt(3, typeIdentification.getId());
            return stm.executeUpdate() > 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM configuracion.TiposIdentificacion WHERE id = ?";
        try(Connection conn = getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public TypeIdentification SearchByName(String name){
        String sql = "SELECT * FROM configuracion.TiposIdentificacion WHERE nombre = ?";
        try(Connection conn = getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return mapToTypeIdentification(rs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
}
