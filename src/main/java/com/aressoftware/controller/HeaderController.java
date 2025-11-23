package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HeaderController {


@FXML private Label lblTitle;
@FXML private Button btnLogout;

// Interfaz para callback al cerrar sesión
public interface LogoutListener {
    void onLogout();
}

private LogoutListener logoutListener;

@FXML
public void initialize() {
    // Puedes inicializar aquí si quieres algún efecto o estilo dinámico
}

public void setTitle(String title) {
    if (lblTitle != null) {
        lblTitle.setText(title);
    }
}

public void setOnLogout(LogoutListener listener) {
    this.logoutListener = listener;
}

@FXML
private void handleLogout() {
    if (logoutListener != null) {
        logoutListener.onLogout();
    }
}


}
