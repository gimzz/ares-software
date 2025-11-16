package com.aressoftware.test;

import java.util.List;

import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.User;

public class TestUserDAO {

    public static void main(String[] args) {

        System.out.println("===== PRUEBAS UserDAO =====");

        UserDAO userDAO = new UserDAO();

        // 1. Probar conexión (findAll)
        System.out.println("\n→ Probando conexión y findAll()...");
        List<User> users = userDAO.findAll();
        System.out.println("Usuarios encontrados: " + users.size());
        for (User u : users) {
            System.out.println(" - " + u.getId() + " | " + u.getUsuario() + " | " + u.getEstado());
        }

        // 2. Probar findByUsername
        System.out.println("\n→ Probando findByUsername('admin')...");
        User admin = userDAO.findByUsername("admin");
        if (admin != null) {
            System.out.println("Usuario encontrado: " + admin.getUsuario());
        } else {
            System.out.println("No existe usuario admin");
        }

        // 3. Probar insertar usuario
        System.out.println("\n→ Probando inserción de usuario...");

        User nuevo = new User();
        nuevo.setNombre("Tester QA");
        nuevo.setUsuario("tester");
        nuevo.setPassword("test123");
        nuevo.setIdRol(2);
        nuevo.setEstado("activo");

        boolean creado = userDAO.create(nuevo);
        System.out.println("Usuario creado?: " + creado);

        // 4. Probar actualización (sólo datos generales, NO password)
        System.out.println("\n→ Probando updateUserData() del usuario tester...");
        User tester = userDAO.findByUsername("tester");
        if (tester != null) {
            tester.setNombre("Tester QA Senior");
            tester.setEstado("inactivo");
            boolean actualizado = userDAO.updateUserData(tester); // ¡no toca el password!
            System.out.println("Actualizado (datos generales)?: " + actualizado);
        }

        // 5. Probar cambio de estado
        if (tester != null) {
            System.out.println("\n→ Probando cambiar estado...");
            boolean cambio = userDAO.cambiarEstado(tester.getId(), "activo");
            System.out.println("Estado cambiado?: " + cambio);
        }

        // 6. Probar cambio de contraseña
        if (tester != null) {
            System.out.println("\n→ Probando cambio de contraseña...");
            boolean cambioPass = userDAO.updatePassword(tester.getId(), "nuevaClave123");
            System.out.println("Contraseña cambiada?: " + cambioPass);
        }

        // 7. Probar login con nueva contraseña (debe ser exitoso)
        System.out.println("\n→ Probando login con nueva contraseña...");
        boolean loginExitoso = userDAO.checkLogin("tester", "nuevaClave123");
        System.out.println("Login exitoso con nueva contraseña?: " + loginExitoso);

        // 8. Probar login con contraseña anterior (debe fallar)
        System.out.println("\n→ Probando login con contraseña anterior...");
        boolean loginAnterior = userDAO.checkLogin("tester", "test123");
        System.out.println("Login exitoso con contraseña anterior?: " + loginAnterior);

        // 9. Probar login con cualquier contraseña incorrecta
        System.out.println("\n→ Probando login fallido...");
        boolean loginFallido = userDAO.checkLogin("tester", "contraseñaIncorrecta");
        System.out.println("Login fallido?: " + loginFallido);

        System.out.println("\n===== FIN DE PRUEBAS =====");
    }
}