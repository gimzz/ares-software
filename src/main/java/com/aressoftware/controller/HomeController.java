package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.aressoftware.model.security.User;
import com.aressoftware.dao.UserDAO;

import java.io.IOException;

public class HomeController {

    @FXML private BorderPane root;
    @FXML private VBox navContainer;
    @FXML private VBox headerContainer;
    @FXML private VBox contentContainer;

    private HeaderController headerController;
    private UserDAO userDAO;

    @FXML
    private void initialize() {
        try {
            userDAO = new UserDAO(); // inicializa el DAO una sola vez
        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo inicializar UserDAO: " + e.getMessage());
        }

        loadHeader();
        loadSideMenu();
        loadView("InicioView.fxml"); // Vista inicial ahora es Inicio

        // Cargar CSS global cuando la escena esté lista
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                try {
                    String css = getClass().getResource("/css/app.css").toExternalForm();
                    newScene.getStylesheets().add(css);
                    System.out.println("[INFO] CSS cargado correctamente");
                } catch (Exception e) {
                    System.out.println("[WARN] No se pudo cargar app.css");
                }
            }
        });
    }

    private void loadHeader() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HeaderView.fxml"));
            Node headerNode = loader.load();
            headerController = loader.getController();
            headerContainer.getChildren().add(headerNode);
            headerController.setHomeController(this);
            headerController.setWelcomeText("Bienvenido al panel de Ares Software");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadSideMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NavMenuSide.fxml"));
            VBox navMenu = loader.load();
            NavMenuController navController = loader.getController();
            navController.setHomeController(this);
            navContainer.getChildren().setAll(navMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxml));
            Parent view = loader.load();

            // Si es Dashboard, inyecta el DAO y carga datos
            if ("DashboardView.fxml".equals(fxml)) {
                DashboardController dashController = loader.getController();
                dashController.setUserDao(userDAO);
                dashController.loadData();
            }

            contentContainer.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(User user) {
        if (user != null && headerController != null) {
            headerController.setWelcomeText("Bienvenido, " + user.getNombre());
        }
    }

    // Método público para logout (llamado desde NavMenuController)
    public void doLogout() {
        try {
            logoutToLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logoutToLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Parent loginRoot = loader.load();

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(loginRoot));
        stage.setTitle("Ares Software - Login");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.centerOnScreen();
    }
}