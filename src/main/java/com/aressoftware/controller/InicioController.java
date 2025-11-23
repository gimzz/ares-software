package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class InicioController {

    @FXML private VBox inicioRoot;

    @FXML
    public void initialize() {
        // Aquí puedes cargar datos iniciales dinámicos si lo deseas
        System.out.println("InicioView cargado correctamente.");
    }
}