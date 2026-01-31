package com.aressoftware.model.configuration;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Entity {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty apellido;
    private SimpleIntegerProperty id_tipo_identificacion;
    private SimpleStringProperty numero_identificacion;
    private SimpleStringProperty telefono_celular;
    private SimpleStringProperty direccion;
    private SimpleStringProperty email;

    public Entity() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.apellido = new SimpleStringProperty();
        this.id_tipo_identificacion = new SimpleIntegerProperty();
        this.numero_identificacion = new SimpleStringProperty();
        this.telefono_celular = new SimpleStringProperty();
        this.direccion = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public Entity(int id, String nombre, String apellido, int id_tipo_identificacion, String numero_identificacion, String telefono_celular, String direccion, String email) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.id_tipo_identificacion = new SimpleIntegerProperty(id_tipo_identificacion);
        this.numero_identificacion = new SimpleStringProperty(numero_identificacion);
        this.telefono_celular = new SimpleStringProperty(telefono_celular);
        this.direccion = new SimpleStringProperty(direccion);
        this.email = new SimpleStringProperty(email);
    }
    
    public int getId(){
        return id.get();
    }

    public void setId(int id){
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty(){
        return id;
    }

    public String getNombre(){
        return nombre.get();
    }

    public void setNombre(String nombre){
        this.nombre.set(nombre);
    }

    public SimpleStringProperty nombreProperty(){
        return nombre;
    }

    public String getApellido(){
        return apellido.get();
    }

    public void setApellido(String apellido){
        this.apellido.set(apellido);
    }

    public SimpleStringProperty apellidoProperty(){
        return apellido;
    }

    public int getIdTipoIdentificacion(){
        return id_tipo_identificacion.get();
    }

    public void setIdTipoIdentificacion(int id_tipo_identificacion){
        this.id_tipo_identificacion.set(id_tipo_identificacion);
    }

    public SimpleIntegerProperty idTipoIdentificacionProperty(){
        return id_tipo_identificacion;
    }

    public String getNumeroIdentificacion(){
        return numero_identificacion.get();
    }

    public void setNumeroIdentificacion(String numero_identificacion){
        this.numero_identificacion.set(numero_identificacion);
    }

    public SimpleStringProperty numeroIdentificacionProperty(){
        return numero_identificacion;
    }

    public String getTelefonoCelular(){
        return telefono_celular.get();
    }

    public void setTelefonoCelular(String telefono_celular){
        this.telefono_celular.set(telefono_celular);
    }

    public SimpleStringProperty telefonoCelularProperty(){
        return telefono_celular;
    }

    public String getDireccion(){
        return direccion.get();
    }

    public void setDireccion(String direccion){
        this.direccion.set(direccion);
    }

    public SimpleStringProperty direccionProperty(){
        return direccion;
    }

    public String getEmail(){
        return email.get();
    }

    public void setEmail(String email){
        this.email.set(email);
    }

    public SimpleStringProperty emailProperty(){
        return email;
    }


}
