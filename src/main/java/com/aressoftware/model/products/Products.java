package com.aressoftware.model.products;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
    

public class Products {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty idCategoria;
    private SimpleIntegerProperty idMarca;
    private SimpleIntegerProperty idUnidadMedida;
    private SimpleStringProperty nombre;
    private SimpleStringProperty descripcion;

    public Products() {
        this.id = new SimpleIntegerProperty();
        this.idCategoria = new SimpleIntegerProperty();
        this.idMarca = new SimpleIntegerProperty();
        this.idUnidadMedida = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
    }

    public Products(int id, int idCategory, int idMarca, int idUnidadMedida, String name, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.idCategoria = new SimpleIntegerProperty(idCategory);
        this.idMarca = new SimpleIntegerProperty(idMarca);
        this.idUnidadMedida = new SimpleIntegerProperty(idUnidadMedida);
        this.nombre = new SimpleStringProperty(name);
        this.descripcion = new SimpleStringProperty(description);
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

    public int getIdCategoria() {
        return idCategoria.get();
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria.set(idCategoria);
    }

    public SimpleIntegerProperty idCategoryProperty() {
        return idCategoria;
    }

    public int getIdMarca() {
        return idMarca.get();
    }

    public void setIdMarca(int idMarca) {
        this.idMarca.set(idMarca);
    }

    public SimpleIntegerProperty idMarcaProperty() {
        return idMarca;
    }

    public int getIdUnidadMedida() {
        return idUnidadMedida.get();
    }

    public void setIdUnidadMedida(int idUnidadMedida) {
        this.idUnidadMedida.set(idUnidadMedida);
    }

    public SimpleIntegerProperty idUnidadMedidaProperty() {
        return idUnidadMedida;
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

    public void setDescription(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }   
}
