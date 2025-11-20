package com.aressoftware.test;

import com.aressoftware.dao.ModuleDAO;
import com.aressoftware.dao.RolesDAO;
import com.aressoftware.dao.RolesPermissionsDAO;
import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.Module;
import com.aressoftware.model.security.Roles;
import com.aressoftware.model.security.RolesPermissions;
import com.aressoftware.model.security.User;

public class SecurityTest {

    public static void main(String[] args) {

        System.out.println("===== TEST SECURITY =====");

        UserDAO userDAO = new UserDAO();
        RolesDAO rolesDAO = new RolesDAO();
        ModuleDAO moduleDAO = new ModuleDAO();
        RolesPermissionsDAO rpDAO = new RolesPermissionsDAO();

        System.out.println("\n Roles existentes:");
        rolesDAO.findAll().forEach(r -> System.out.println(" - " + r.getNombre()));

        System.out.println("\nMódulos existentes:");
        moduleDAO.findAll().forEach(m -> System.out.println(" - " + m.getNombre()));

        int testRoleId = 1;

        System.out.println("\n Permisos del rol ID = " + testRoleId);
        rpDAO.findByRole(testRoleId).forEach(p ->
                System.out.println(" - Modulo ID " + p.getModuleId() + " -> Permiso: " + p.getPermission())
        );

        boolean canEdit = rpDAO.hasPermission(testRoleId, 1, "editar");
        System.out.println("\n¿Rol 1 puede EDITAR en módulo 1? -> " + canEdit);
    }
}
