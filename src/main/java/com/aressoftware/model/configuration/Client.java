package com.aressoftware.model.configuration;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty idEntidad;
    private SimpleIntegerProperty idTipo;
    private SimpleIntegerProperty idCondicionPago;

    public Client() {
        this.id = new SimpleIntegerProperty();
        this.idEntidad = new SimpleIntegerProperty();
        this.idTipo = new SimpleIntegerProperty();
        this.idCondicionPago = new SimpleIntegerProperty();
    }

    public Client(int id, int idEntidad, int idTipo, int idCondicionPago) {
        this.id = new SimpleIntegerProperty(id);
        this.idEntidad = new SimpleIntegerProperty(idEntidad);
        this.idTipo = new SimpleIntegerProperty(idTipo);
        this.idCondicionPago = new SimpleIntegerProperty(idCondicionPago);
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

    public int getIdEntidad(){
        return idEntidad.get();
    }

    public void setIdEntidad(int idEntidad){
        this.idEntidad.set(idEntidad);
    }

    public SimpleIntegerProperty idEntidadProperty(){
        return idEntidad;
    }

    public int getIdTipo(){
        return idTipo.get();
    }

    public void setIdTipo(int idTipo){
        this.idTipo.set(idTipo);
    }

    public SimpleIntegerProperty idTipoProperty(){
        return idTipo;
    }

    public int getIdCondicionPago(){
        return idCondicionPago.get();
    }

    public void setIdCondicionPago(int idCondicionPago){
        this.idCondicionPago.set(idCondicionPago);
    }

    public SimpleIntegerProperty idCondicionPagoProperty(){
        return idCondicionPago;
    }



}
