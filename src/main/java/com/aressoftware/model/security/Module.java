package com.aressoftware.model.security;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Module
 *
 * Representa un módulo del sistema, por ejemplo:
 *  - Ventas
 *  - Compras
 *  - Inventario
 *  - Contabilidad
 *
 * Campo "activo": se usa para bloquear módulos sin eliminarlos.
 */
public class Module {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty descripcion;
    private SimpleBooleanProperty activo;

    public Module() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.activo = new SimpleBooleanProperty(true);
    }

    public Module(int id, String nombre, String descripcion, boolean activo) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.activo = new SimpleBooleanProperty(activo);
    }


    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public SimpleIntegerProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public SimpleStringProperty nombreProperty() { return nombre; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public SimpleStringProperty descripcionProperty() { return descripcion; }

    public boolean isActivo() { return activo.get(); }
    public void setActivo(boolean activo) { this.activo.set(activo); }
    public SimpleBooleanProperty activoProperty() { return activo; }
}
