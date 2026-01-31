package com.aressoftware.controller;

import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.User;
import com.aressoftware.util.PasswordUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;
    @FXML private Button btnLogin;
    @FXML private ImageView imageView;

    private UserDAO userDAO;

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Ingrese usuario y contraseña");
            return;
        }

        try {
            System.out.println("[DEBUG] Intento de login -> usuario='" + username + "'");

            // Si userDAO no está inicializado, intentar crear uno (fallback)
            if (userDAO == null) {
                try {
                    userDAO = new UserDAO();
                } catch (Exception ex) {
                    System.out.println("[ERROR] No se pudo inicializar UserDAO: " + ex.getMessage());
                }
            }

            // No usar fallback hardcoded: autenticar siempre contra la BD

            if (userDAO == null) {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("Error interno: DAO no disponible");
                return;
            }

            User user = userDAO.findByUsername(username);
            System.out.println("[DEBUG] Resultado findByUsername: " + (user == null ? "null" : user.toString()));

            if (user == null) {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("Usuario no encontrado");
                return;
            }

            // Verificar contraseña: soporta BCrypt (hash) o texto plano en BD
            String stored = user.getPassword();
            boolean passwordMatches = false;
            if (stored != null) {
                try {
                    if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                        passwordMatches = PasswordUtils.checkPassword(password, stored);
                    } else {
                        passwordMatches = stored.equals(password);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (!passwordMatches) {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("Contraseña incorrecta");
                return;
            }

            if (user.getEstado() != null && !user.getEstado().equalsIgnoreCase("activo")) {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("Usuario inactivo");
                return;
            }

            // Login correcto — abrir Home
            abrirHomeConUsuario(user, false);

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Error al iniciar sesión: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Inicializar DAO aquí (no en constructor) y cargar imagen del login
        try {
            userDAO = new UserDAO();
        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo crear UserDAO en initialize(): " + e.getMessage());
        }

        // Cargar logo si existe
        try {
            InputStream is = getClass().getResourceAsStream("/img/logo.png");
            if (is != null && imageView != null) {
                Image img = new Image(is);
                imageView.setImage(img);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen del login: " + e.getMessage());
        }
        // Diagnostics
        System.out.println("[INIT] LoginController.initialize - userDAO=" + (userDAO != null) + ", imageView=" + (imageView != null));
        if (lblMessage != null) lblMessage.setText("");

        // Aplicar stylesheet global (app.css) cuando la Scene esté disponible
        try {
            String css = getClass().getResource("/css/app.css").toExternalForm();
            if (txtUsername != null) {
                txtUsername.sceneProperty().addListener((obs, oldScene, newScene) -> {
                    if (newScene != null) {
                        if (!newScene.getStylesheets().contains(css)) newScene.getStylesheets().add(css);
                    }
                });
            }
        } catch (Exception ex) {
            // ignore if css not found
        }
    }

    private void abrirHomeConUsuario(User user, boolean demo) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomeView.fxml"));
        Parent root = loader.load();
        HomeController homeController = loader.getController();
        if (homeController != null) homeController.setCurrentUser(user);
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Ares Software - Home" + (demo ? " (demo)" : ""));
        // maximizar la ventana al entrar al Home
        try {
            stage.setMaximized(true);
            stage.setResizable(true);
        } catch (Exception ex) {
            // ignore if not supported
        }
    }
}