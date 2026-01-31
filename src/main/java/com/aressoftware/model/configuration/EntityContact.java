package com.aressoftware.model.configuration;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class EntityContact {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty idEntidad;
    private SimpleStringProperty email;
    private SimpleStringProperty telefono;
    private SimpleStringProperty cargo;
    private SimpleBooleanProperty esFacturacion;
    private SimpleBooleanProperty esEnvio;

    public EntityContact() {
        this.id = new SimpleIntegerProperty();
        this.idEntidad = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty();
        this.telefono = new SimpleStringProperty();
        this.cargo = new SimpleStringProperty();
        this.esFacturacion = new SimpleBooleanProperty();
        this.esEnvio = new SimpleBooleanProperty();
    }
    public EntityContact(int id, int idEntidad, String email, String telefono, String cargo, boolean esFacturacion, boolean esEnvio) {
        this.id = new SimpleIntegerProperty(id);
        this.idEntidad = new SimpleIntegerProperty(idEntidad);
        this.email = new SimpleStringProperty(email);
        this.telefono = new SimpleStringProperty(telefono);
        this.cargo = new SimpleStringProperty(cargo);
        this.esFacturacion = new SimpleBooleanProperty(esFacturacion);
        this.esEnvio = new SimpleBooleanProperty(esEnvio);
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

    public String getEmail(){
        return email.get();
    }

    public void setEmail(String email){
        this.email.set(email);
    }

    public SimpleStringProperty emailProperty(){
        return email;
    }

    public String getTelefono(){
        return telefono.get();
    }

    public void setTelefono(String telefono){
        this.telefono.set(telefono);
    }

    public SimpleStringProperty telefonoProperty(){
        return telefono;
    }

    public String getCargo(){
        return cargo.get();
    }

    public void setCargo(String cargo){
        this.cargo.set(cargo);
    }

    public SimpleStringProperty cargoProperty(){
        return cargo;
    }

    public boolean isEsFacturacion(){
        return esFacturacion.get();
    }

    public void setEsFacturacion(boolean esFacturacion){
        this.esFacturacion.set(esFacturacion);
    }

    public SimpleBooleanProperty esFacturacionProperty(){
        return esFacturacion;
    }

    public boolean isEsEnvio(){
        return esEnvio.get();
    }

    public void setEsEnvio(boolean esEnvio){
        this.esEnvio.set(esEnvio);
    }

    public SimpleBooleanProperty esEnvioProperty(){
        return esEnvio;
    }
    

}
