package com.aressoftware.model.security;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Roles {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty descripcion;

    public Roles() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
    }

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

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

}
