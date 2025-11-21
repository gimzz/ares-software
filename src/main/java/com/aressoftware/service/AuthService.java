package com.aressoftware.service;

import com.aressoftware.dao.UserDAO;
import com.aressoftware.dao.RolesPermissionsDAO;
import com.aressoftware.dao.ModuleDAO;
import com.aressoftware.model.security.User;
import com.aressoftware.model.security.Module;
import com.aressoftware.model.security.RolesPermissions;
import com.aressoftware.util.PasswordUtils;

import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private final UserDAO userDAO;
    private final RolesPermissionsDAO rolesPermissionsDAO;
    private final ModuleDAO moduleDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.rolesPermissionsDAO = new RolesPermissionsDAO();
        this.moduleDAO = new ModuleDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            System.out.println("❌ Usuario no encontrado");
            return null;
        }

        if (!PasswordUtils.checkPassword(password, user.getPassword())) {
            System.out.println("❌ Contraseña incorrecta");
            return null;
        }

        if (!user.getEstado().equalsIgnoreCase("ACTIVO")) {
            System.out.println("❌ El usuario está inactivo.");
            return null;
        }

        System.out.println("✔ Login exitoso");
        return user;
    }

    public boolean hasPermission(User user, String moduleName, String action) {
        Module module = moduleDAO.findByName(moduleName);
        if (module == null) {
            System.out.println("❌ Módulo no encontrado: " + moduleName);
            return false;
        }

        return rolesPermissionsDAO.hasPermission(
                user.getIdRol(),
                module.getId(),
                action);
    }

    public List<RolesPermissions> getUserPermissions(User user) {
        return rolesPermissionsDAO.findByRole(user.getIdRol());
    }

    public List<Module> getVisibleModules(User user) {
        List<Module> allModules = moduleDAO.findAll();
        List<Module> visible = new ArrayList<>();

        for (Module m : allModules) {
            boolean canSee = rolesPermissionsDAO.hasPermission(
                    user.getIdRol(),
                    m.getId(),
                    "ver");

            if (canSee) {
                visible.add(m);
            }
        }

        return visible;
    }

}
