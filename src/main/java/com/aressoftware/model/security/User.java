package com.aressoftware.model.security;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty usuario;
    private SimpleStringProperty password; // almacena el hash bcrypt
    private SimpleIntegerProperty idRol;
    private SimpleStringProperty estado;

    public User() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.usuario = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.idRol = new SimpleIntegerProperty();
        this.estado = new SimpleStringProperty();
    }

    public User(int id, String nombre, String usuario, String password, int idRol, String estado) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.usuario = new SimpleStringProperty(usuario);
        this.password = new SimpleStringProperty(password); 
        this.idRol = new SimpleIntegerProperty(idRol);
        this.estado = new SimpleStringProperty(estado);
    }

    // Getters y Setters
    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }
    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public String getUsuario() {
        return usuario.get();
    }
    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }
    public SimpleStringProperty usuarioProperty() {
        return usuario;
    }

    public String getPassword() {
        return password.get();
    }
    public void setPassword(String password) {
        this.password.set(password);
    }
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public int getIdRol() {
        return idRol.get();
    }
    public void setIdRol(int idRol) {
        this.idRol.set(idRol);
    }
    public SimpleIntegerProperty idRolProperty() {
        return idRol;
    }

    public String getEstado() {
        return estado.get();
    }
    public void setEstado(String estado) {
        this.estado.set(estado);
    }
    public SimpleStringProperty estadoProperty() {
        return estado;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", usuario='" + getUsuario() + '\'' +
                ", idRol=" + getIdRol() +
                ", estado='" + getEstado() + '\'' +
                '}';
    }
}