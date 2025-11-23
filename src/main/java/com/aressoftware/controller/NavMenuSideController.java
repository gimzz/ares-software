package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NavMenuSideController {

    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu);
    }

    private OnMenuSelectedListener listener;

    public void setOnMenuSelected(OnMenuSelectedListener listener) {
        this.listener = listener;
    }

    @FXML
    private ImageView logoImage;

    @FXML
    private void handleInicio() {
        if (listener != null) listener.onMenuSelected("inicio");
    }

    @FXML
    private void handleProductos() {
        if (listener != null) listener.onMenuSelected("productos");
    }

    @FXML
    private void handleUsuarios() {
        if (listener != null) listener.onMenuSelected("usuarios");
    }

    @FXML
    private void handleVentas() {
        if (listener != null) listener.onMenuSelected("ventas");
    }

    @FXML
    private void initialize() {
        try {
            Image logo = new Image(getClass().getResource("/img/logo.png").toExternalForm());
            logoImage.setImage(logo);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo: " + e.getMessage());
        }
    }
}