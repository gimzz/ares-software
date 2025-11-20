package com.aressoftware.model.security;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * RolesPermissions
 *
 * Representa un permiso que un rol tiene sobre un módulo.
 * Ejemplo:
 *   Rol = Vendedor (id_rol = 2)
 *   Módulo = Ventas (id_modulo = 1)
 *   Permiso = "crear"
 */
public class RolesPermissions {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty roleId;
    private SimpleIntegerProperty moduleId;
    private SimpleStringProperty permission;

    public RolesPermissions() {
        this.id = new SimpleIntegerProperty();
        this.roleId = new SimpleIntegerProperty();
        this.moduleId = new SimpleIntegerProperty();
        this.permission = new SimpleStringProperty();
    }

    public RolesPermissions(int id, int roleId, int moduleId, String permission) {
        this.id = new SimpleIntegerProperty(id);
        this.roleId = new SimpleIntegerProperty(roleId);
        this.moduleId = new SimpleIntegerProperty(moduleId);
        this.permission = new SimpleStringProperty(permission);
    }

   
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public SimpleIntegerProperty idProperty() { return id; }

    public int getRoleId() { return roleId.get(); }
    public void setRoleId(int roleId) { this.roleId.set(roleId); }
    public SimpleIntegerProperty roleIdProperty() { return roleId; }

    public int getModuleId() { return moduleId.get(); }
    public void setModuleId(int moduleId) { this.moduleId.set(moduleId); }
    public SimpleIntegerProperty moduleIdProperty() { return moduleId; }

    public String getPermission() { return permission.get(); }
    public void setPermission(String permission) { this.permission.set(permission); }
    public SimpleStringProperty permissionProperty() { return permission; }
}
