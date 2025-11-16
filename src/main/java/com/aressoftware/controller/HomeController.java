package com.aressoftware.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.application.Platform;

import com.aressoftware.model.security.User;

public class HomeController {

    @FXML private Label lblWelcome;
    @FXML private PieChart pieChartDashboard;
    @FXML private TableView<?> tblUsers;
    @FXML private TableView<?> tblActivity;
    @FXML private Button btnLogout;

    @FXML
    private void initialize() {
        // Texto de bienvenida simple
        if (lblWelcome != null) {
            lblWelcome.setText("Bienvenido al panel de Ares Software");
        }

        // Datos de ejemplo para el PieChart
        if (pieChartDashboard != null) {
            pieChartDashboard.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Activos", 72),
                    new PieChart.Data("Inactivos", 28)
            ));
        }
    }

    /**
     * Recibe el usuario autenticado y actualiza la vista.
     */
    public void setCurrentUser(User user) {
        if (lblWelcome != null && user != null) {
            lblWelcome.setText("Bienvenido, " + user.getNombre());
        }
    }

    @FXML
    private void handleLogout() {
        // Manejo simple: salir de la app. En tu app real deberías cambiar de escena al Login.
        Platform.exit();
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
