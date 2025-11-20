package com.aressoftware.test;

import com.aressoftware.service.AuthService;
import com.aressoftware.model.security.User;

public class AuthServiceTest {

    public static void main(String[] args) {

        AuthService auth = new AuthService();

        System.out.println("===== TEST AUTH SERVICE =====");

        // Probar login con usuario y contraseña
        System.out.println("\n🔐 Probando LOGIN...");
        User user = auth.login("tester", "nuevaClave123");

        if (user == null) {
            System.out.println("❌ Login fallido");
            return;
        }

        System.out.println("✔ Login correcto. Usuario: " + user.getNombre());

        // Probar permisos usuario
        System.out.println("\n📌 Probando permisos...");
        boolean puedeEditar = auth.hasPermission(user, "Ventas", "editar");
        boolean puedeEliminar = auth.hasPermission(user, "Inventario", "eliminar");
        boolean puedeVerContabilidad = auth.hasPermission(user, "Contabilidad", "ver");

        System.out.println("¿Puede editar Ventas? -> " + puedeEditar);
        System.out.println("¿Puede eliminar Inventario? -> " + puedeEliminar);
        System.out.println("¿Puede ver Contabilidad? -> " + puedeVerContabilidad);

        // Listar todos los permisos
        System.out.println("\n📎 Permisos del usuario:");
        auth.getUserPermissions(user).forEach(p ->
                System.out.println(" - Módulo ID " + p.getModuleId() + " -> " + p.getPermission())
        );

        System.out.println("\n===== FIN TEST AUTH SERVICE =====");
    }
}
