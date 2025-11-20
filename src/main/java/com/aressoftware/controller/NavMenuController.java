package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavMenuController {

    @FXML private Button btnInicioSide;
    @FXML private Button btnDashboardSide;
    @FXML private Button btnUsuariosSide;
    @FXML private Button btnClientesSide;
    @FXML private Button btnVentasSide;
    @FXML private Button btnInventarioSide;
    @FXML private Button btnLogoutSide;

    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    public void initialize() {
        btnInicioSide.setOnAction(e -> homeController.loadView("InicioView.fxml"));
        btnDashboardSide.setOnAction(e -> homeController.loadView("DashboardView.fxml"));
        btnUsuariosSide.setOnAction(e -> homeController.loadView("UsuariosView.fxml"));
        btnClientesSide.setOnAction(e -> homeController.loadView("ClientesView.fxml"));
        btnVentasSide.setOnAction(e -> homeController.loadView("VentasView.fxml"));
        btnInventarioSide.setOnAction(e -> homeController.loadView("InventarioView.fxml"));

        btnLogoutSide.setOnAction(e -> homeController.doLogout());
    }
}