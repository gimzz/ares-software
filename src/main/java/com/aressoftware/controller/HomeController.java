package com.aressoftware.controller;

import com.aressoftware.model.security.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

// 💡 IMPORTANTE: Necesitas esta importación para definir el margen
import javafx.geometry.Insets; 

import java.io.IOException;
import java.net.URL;

public class HomeController {

    @FXML private BorderPane rootPane; 
    @FXML private AnchorPane mainContent; 

    private NavMenuSideController navController;
    private HeaderController headerController;

    private User currentUser;

    @FXML
    public void initialize() {
        try {
            // --- Cargar NavMenuSide ---
            FXMLLoader navLoader = new FXMLLoader(getClass().getResource("/components/nav/NavMenuSide.fxml"));
            Parent navContent = navLoader.load();
            navController = navLoader.getController();
            
            // 🎯 SOLUCIÓN: Aplicar margen superior al menú lateral
            double headerHeight = 2.0; // AJUSTA ESTE VALOR: Altura de tu Header + espacio deseado
            BorderPane.setMargin(navContent, new Insets(headerHeight, 0, 0, 0));
            
            rootPane.setLeft(navContent);

            // --- Cargar Header ---
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/components/header/Header.fxml"));
            Parent headerContent = headerLoader.load();
            headerController = headerLoader.getController();
            rootPane.setTop(headerContent);

            // Configurar eventos
            navController.setOnMenuSelected(this::onMenuSelected);
            headerController.setOnLogout(this::cerrarSesion);

            // Cargar vista inicial
            loadView("/layout/InicioView.fxml");

            // --- Aplicar CSS global cuando la Scene esté lista ---
            rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    URL cssUrl = getClass().getResource("/styles/global.css");
                    if (cssUrl != null && !newScene.getStylesheets().contains(cssUrl.toExternalForm())) {
                        newScene.getStylesheets().add(cssUrl.toExternalForm());
                        System.out.println("CSS global cargado correctamente");
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Recibir el usuario loggeado y mostrarlo en el header
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (headerController != null && user != null) {
            // Mostrar saludo con el nombre del usuario
            headerController.setTitle("Bienvenido, " + user.getNombre()); 
            // Si tu clase User usa getUsuario(), cámbialo por getUsuario()
        }
    }

    // Método llamado desde NavMenu
    private void onMenuSelected(String menu) {
        if (currentUser == null) return;

        switch (menu) {
            case "inicio":
                headerController.setTitle("Bienvenido, " + currentUser.getNombre() + " - Inicio");
                loadView("/layout/InicioView.fxml");
                break;
            case "productos":
                headerController.setTitle("Bienvenido, " + currentUser.getNombre() + " - Productos");
                loadView("/layout/ProductosView.fxml");
                break;
            case "usuarios":
                headerController.setTitle("Bienvenido, " + currentUser.getNombre() + " - Usuarios");
                loadView("/layout/UsuariosView.fxml");
                break;
            case "ventas":
                headerController.setTitle("Bienvenido, " + currentUser.getNombre() + " - Ventas");
                loadView("/layout/VentasView.fxml");
                break;
        }
    }

    // Cargar una vista en el centro
    private void loadView(String fxmlPath) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) throw new IOException("FXML no encontrado: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent content = loader.load();
            mainContent.getChildren().setAll(content);

            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);

        } catch (IOException e) {
            System.out.println("No se pudo cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // Cerrar sesión y volver al login
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/LoginView.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}