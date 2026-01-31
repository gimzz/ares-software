package com.aressoftware.test;

import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.User;

import java.util.List;

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

        // 3. Comprobar si existe y si no crear usuario con contraseña en texto plano
        User tester = userDAO.findByUsername("tester");
        if (tester == null) {
            User nuevo = new User();
            nuevo.setNombre("Tester QA");
            nuevo.setUsuario("tester");
            nuevo.setPassword("test123"); // CONTRASEÑA EN TEXTO PLANO SIN HASH AQUI
            nuevo.setIdRol(2);
            nuevo.setEstado("activo");

            boolean creado = userDAO.create(nuevo);
            System.out.println("Usuario creado?: " + creado);
            tester = userDAO.findByUsername("tester");
        } else {
            System.out.println("Usuario 'tester' ya existe, usándolo para pruebas.");
        }

        // 4. Actualizar solo datos generales, no password
        if (tester != null) {
            tester.setNombre("Tester QA Senior");
            tester.setEstado("inactivo");
            boolean actualizado = userDAO.updateUserData(tester);
            System.out.println("Actualizado (datos generales)?: " + actualizado);
        }

        // 5. Cambiar estado a activo
        if (tester != null) {
            boolean cambio = userDAO.cambiarEstado(tester.getId(), "activo");
            System.out.println("Estado cambiado?: " + cambio);
        }

        // 6. Cambiar contraseña SIN hacer hash aquí (updatePassword ya hace hash)
        if (tester != null) {
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

        // 9. Probar login con contraseña incorrecta (debe fallar)
        System.out.println("\n→ Probando login fallido...");
        boolean loginFallido = userDAO.checkLogin("tester", "contraseñaIncorrecta");
        System.out.println("Login fallido?: " + loginFallido);

        System.out.println("\n===== FIN DE PRUEBAS =====");
    }
}
