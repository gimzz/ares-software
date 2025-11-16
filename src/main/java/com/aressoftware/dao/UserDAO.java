package com.aressoftware.dao;

import com.aressoftware.config.DatabaseConfig;
import com.aressoftware.dao.BaseDAO;
import com.aressoftware.model.security.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO {

    // ==========================================================
    //  MAPEAR RESULTSET → USER MODEL
    // ==========================================================
    /**
     * Convierte un registro de la base de datos (fila del ResultSet)
     * en un objeto User de Java.
     *
     * @param rs ResultSet que contiene los datos de la fila actual
     * @return Objeto User con los datos de la fila
     * @throws SQLException Si ocurre un error al acceder a los datos
     */
    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),             // Obtiene el valor de la columna "id" como entero
                rs.getString("nombre"),      // Obtiene el valor de la columna "nombre" como String
                rs.getString("usuario"),     // Obtiene el valor de la columna "usuario" como String
                rs.getString("contraseña"),  // Obtiene el valor de la columna "contraseña" como String
                rs.getInt("id_rol"),         // Obtiene el valor de la columna "id_rol" como entero
                rs.getString("estado")       // Obtiene el valor de la columna "estado" como String
        );
    }

    // ==========================================================
    //  BUSCAR POR USERNAME (PARA LOGIN)
    // ==========================================================
    /**
     * Busca un usuario por su nombre de usuario en la base de datos.
     *
     * PreparedStatement es un objeto que permite ejecutar consultas SQL
     * de manera segura, evitando inyecciones SQL. El signo ? es un
     * parámetro que se reemplaza por el valor real usando stmt.setX().
     *
     * ResultSet (variable rs) es el resultado de la consulta, básicamente
     * una tabla en memoria que contiene las filas que cumplen la consulta.
     *
     * @param username Nombre de usuario a buscar
     * @return Objeto User si se encuentra, null si no
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM seguridad.usuarios WHERE usuario = ?";

        try (Connection conn = getConnection(); // Obtiene la conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta SQL

            stmt.setString(1, username); // Reemplaza el ? por el valor de username

            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta y obtiene el resultado
            if (rs.next()) { // rs.next() mueve el cursor a la primera fila (si existe)
                return mapToUser(rs); // Convierte la fila a un objeto User
            }

        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores si ocurren
        }

        return null; // No se encontró ningún usuario
    }

    // ==========================================================
    //  BUSCAR POR ID
    // ==========================================================
    /**
     * Busca un usuario por su ID en la base de datos.
     *
     * @param id ID del usuario
     * @return Objeto User si se encuentra, null si no
     */
    public User findById(int id) {
        String sql = "SELECT * FROM seguridad.usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Reemplaza el ? por el valor de id

            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta
            if (rs.next()) { // Si existe una fila, mapear a User
                return mapToUser(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==========================================================
    //  LISTAR TODOS LOS USUARIOS
    // ==========================================================
    /**
     * Obtiene todos los usuarios de la base de datos.
     *
     * Statement es similar a PreparedStatement, pero no permite parámetros
     * dinámicos. Se usa aquí porque no necesitamos filtrar la consulta.
     *
     * @return Lista de todos los usuarios
     */
    public List<User> findAll() {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM seguridad.usuarios ORDER BY id ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) { // Crear Statement para consulta

            ResultSet rs = stmt.executeQuery(sql); // Ejecutar consulta

            while (rs.next()) { // Mientras haya filas
                usuarios.add(mapToUser(rs)); // Mapear cada fila a User y agregar a la lista
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // ==========================================================
    //  CREAR NUEVO USUARIO
    // ==========================================================
    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param user Objeto User con los datos a insertar
     * @return true si se insertó correctamente, false si hubo error
     */
    public boolean create(User user) {
        String sql = "INSERT INTO seguridad.usuarios (nombre, usuario, contraseña, id_rol, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNombre());   // Reemplaza ? con el nombre
            stmt.setString(2, user.getUsuario());  // Reemplaza ? con el usuario
            stmt.setString(3, user.getPassword()); // Reemplaza ? con la contraseña
            stmt.setInt(4, user.getIdRol());       // Reemplaza ? con el id_rol
            stmt.setString(5, user.getEstado());   // Reemplaza ? con el estado

            return stmt.executeUpdate() > 0; // executeUpdate devuelve número de filas afectadas

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================================
    //  ACTUALIZAR USUARIO
    // ==========================================================
    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param user Objeto User con los datos actualizados
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean update(User user) {
        String sql = "UPDATE seguridad.usuarios SET nombre = ?, usuario = ?, contraseña = ?, id_rol = ?, estado = ? " +
                     "WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getUsuario());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getIdRol());
            stmt.setString(5, user.getEstado());
            stmt.setInt(6, user.getId()); // ID del usuario a actualizar

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================================
    //  ACTIVAR / DESACTIVAR USUARIO
    // ==========================================================
    /**
     * Cambia el estado de un usuario (por ejemplo, activo o inactivo)
     *
     * @param id ID del usuario
     * @param nuevoEstado Nuevo estado a asignar
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE seguridad.usuarios SET estado = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado); // Reemplaza ? con el nuevo estado
            stmt.setInt(2, id);             // Reemplaza ? con el ID del usuario

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
