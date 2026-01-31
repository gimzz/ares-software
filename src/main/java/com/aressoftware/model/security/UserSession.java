package com.aressoftware.model.security;

import com.aressoftware.model.security.User;

/**
 * UserSession
 *
 * Clase Singleton que almacena al usuario actualmente autenticado
 * durante toda la ejecución de la aplicación.
 *
 * Se utiliza para:
 * - Obtener el usuario logueado desde cualquier parte del sistema.
 * - Obtener el rol del usuario.
 * - Limpiar sesión al cerrar sesión.
 */
public class UserSession {

    private static UserSession instance;
    private User user;

    // Constructor privado (Singleton)
    private UserSession() {}

    /**
     * Obtiene la instancia única de la sesión.
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Guarda el usuario autenticado en la sesión.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Devuelve el usuario actualmente autenticado.
     */
    public User getUser() {
        return user;
    }

    /**
     * Cierra la sesión actual (logout).
     */
    public void clearSession() {
        this.user = null;
        instance = null;
    }

    /**
     * Devuelve el ID del rol del usuario autenticado.
     */
    public int getRoleId() {
        return (user != null) ? user.getIdRol() : -1;
    }

    /**
     * Verifica si hay un usuario en sesión.
     */
    public boolean isLoggedIn() {
        return user != null;
    }
}
