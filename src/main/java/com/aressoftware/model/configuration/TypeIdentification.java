package com.aressoftware.model.configuration;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TypeIdentification {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty descripcion;

    public TypeIdentification() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
    }

    public TypeIdentification(int id, String nombre, String descripcion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
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
