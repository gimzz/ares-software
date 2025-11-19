package com.aressoftware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import com.aressoftware.model.security.User;
import com.aressoftware.dao.UserDAO;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML private PieChart pieChartDashboard;
    @FXML private TableView<User> tblUsers;   // ahora tipado con User
    @FXML private Button btnLogout;

    @FXML private BorderPane root;
    @FXML private VBox navContainer;
    @FXML private VBox headerContainer;
    @FXML private javafx.scene.control.Tab tabDashboard;

    private boolean isSideMenu = true;
    private Node originalTop;
    private UserDAO userDAO;
    private HeaderController headerController;
    private DashboardController dashboardController;
    @FXML private TableColumn<User, Number> colId;
    @FXML private TableColumn<User, String> colNombre;
    @FXML private TableColumn<User, String> colUsuario;
    @FXML private TableColumn<User, Number> colRol;
    @FXML private TableColumn<User, String> colEstado;
    @FXML private TabPane tabPane;

    @FXML
    private void initialize() {
        // guardar referencia al top original (MenuBar + header)
        originalTop = root != null ? root.getTop() : null;
        // header will be loaded below; default text set after load

        // Inicializar DAO y cargar usuarios
        try {
            userDAO = new UserDAO();
        } catch (Exception ex) {
            System.out.println("[ERROR] No se pudo inicializar UserDAO en HomeController: " + ex.getMessage());
        }

        // Cargar menú lateral por defecto y datos
        // Inicializar DAO
        try {
            userDAO = new UserDAO();
        } catch (Exception ex) {
            System.out.println("[ERROR] No se pudo inicializar UserDAO en HomeController: " + ex.getMessage());
        }

        // Cargar header y dashboard FXMLs (separados)
        try {
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/fxml/HeaderView.fxml"));
            Node headerNode = headerLoader.load();
            headerController = headerLoader.getController();
            if (headerContainer != null) headerContainer.getChildren().add(headerNode);
            headerController.setHomeController(this);
            headerController.setWelcomeText("Bienvenido al panel de Ares Software");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/fxml/DashboardView.fxml"));
            Node dashNode = dashLoader.load();
            dashboardController = dashLoader.getController();
            if (tabDashboard != null) tabDashboard.setContent(dashNode);
            if (dashboardController != null && userDAO != null) {
                dashboardController.setUserDao(userDAO);
                dashboardController.loadData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        loadSideMenu();
        loadUsers();

        // Añadir stylesheet cuando la scene esté lista
        if (root != null) {
            root.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    try {
                        String css = getClass().getResource("/css/app.css").toExternalForm();
                        if (!newScene.getStylesheets().contains(css)) newScene.getStylesheets().add(css);
                    } catch (Exception ex) {
                        // ignore if stylesheet not found
                    }
                }
            });
        }
    }

    /**
     * Recibe el usuario autenticado y actualiza la vista.
     */
    public void setCurrentUser(User user) {
        if (user != null) {
            if (headerController != null) headerController.setWelcomeText("Bienvenido, " + user.getNombre());
        }
    }

    /**
     * Recibe la lista de usuarios y actualiza tanto la tabla como el PieChart.
     */
 public void setUsers(List<User> users) {
    if (tblUsers != null && users != null) {
        ObservableList<User> data = FXCollections.observableArrayList(users);
        tblUsers.setItems(data);

        // Contar activos e inactivos
        long activos = users.stream()
                .filter(u -> "Activo".equalsIgnoreCase(u.getEstado()))
                .count();

        long inactivos = users.stream()
                .filter(u -> "Inactivo".equalsIgnoreCase(u.getEstado()))
                .count();

        if (pieChartDashboard != null) {
            pieChartDashboard.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Activos", activos),
                    new PieChart.Data("Inactivos", inactivos)
            ));
        }
    }
}

    @FXML
    private void handleLogout() {
        // por compatibilidad con FXML anterior, cerrar sesión y volver al login
        try {
            logoutToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    public void handleToggleMenu() {
        try {
            if (isSideMenu) {
                Node topMenu = FXMLLoader.load(getClass().getResource("/fxml/NavMenuTop.fxml"));
                // Mostrar el menú horizontal reemplazando el top actual (evita duplicados)
                root.setTop(topMenu);
                // ocultar el mensaje de bienvenida y el toggle del header cuando se muestra el menú horizontal
                if (headerController != null) {
                    headerController.setWelcomeVisible(false);
                }
                // conectar elementos del menú horizontal (cargado dinámicamente)
                try {
                    // navegación
                    Node btnDashTop = topMenu.lookup("#btnDashboardTop");
                    if (btnDashTop instanceof Button) ((Button) btnDashTop).setOnAction(evt -> selectDashboard());
                    Node btnUsersTop = topMenu.lookup("#btnUsuariosTop");
                    if (btnUsersTop instanceof Button) ((Button) btnUsersTop).setOnAction(evt -> selectUsers());

                    // 'Ajustes' puede ser un MenuButton con MenuItem 'miToggleTop'
                    Node ajustesTopNode = topMenu.lookup("#btnAjustesTop");
                    if (ajustesTopNode != null) {
                        if (ajustesTopNode instanceof javafx.scene.control.MenuButton) {
                            javafx.scene.control.MenuButton mb = (javafx.scene.control.MenuButton) ajustesTopNode;
                            for (javafx.scene.control.MenuItem mi : mb.getItems()) {
                                if ("miToggleTop".equals(mi.getId())) {
                                    mi.setOnAction(evt -> handleToggleMenu());
                                }
                            }
                        } else if (ajustesTopNode instanceof Button) {
                            ((Button) ajustesTopNode).setOnAction(evt -> handleToggleMenu());
                        }
                    }

                    // cierre de sesión
                    Node logoutTop = topMenu.lookup("#btnLogoutTop");
                    if (logoutTop instanceof Button) ((Button) logoutTop).setOnAction(evt -> {
                        try { logoutToLogin(); } catch (Exception ex) { ex.printStackTrace(); }
                    });
                } catch (Exception ex) {
                    // ignore lookup issues
                }
                root.setLeft(null);
            } else {
                Node sideMenu = FXMLLoader.load(getClass().getResource("/fxml/NavMenuSide.fxml"));
                // restaurar top original y colocar el menú lateral
                if (originalTop != null) root.setTop(originalTop); else root.setTop(null);
                root.setLeft(sideMenu);
                // asegurar que el side menu ocupe todo el alto y conectar su navegación y botón de logout
                try {
                    if (sideMenu instanceof Region) ((Region) sideMenu).setPrefHeight(Double.MAX_VALUE);
                    Node logoutSide = sideMenu.lookup("#btnLogoutSide");
                    if (logoutSide instanceof Button) ((Button) logoutSide).setOnAction(evt -> {
                        try { logoutToLogin(); } catch (Exception ex) { ex.printStackTrace(); }
                    });
                    Node btnDashSide = sideMenu.lookup("#btnDashboardSide");
                    if (btnDashSide instanceof Button) ((Button) btnDashSide).setOnAction(evt -> selectDashboard());
                    Node btnUsersSide = sideMenu.lookup("#btnUsuariosSide");
                    if (btnUsersSide instanceof Button) ((Button) btnUsersSide).setOnAction(evt -> selectUsers());
                    Node btnAjustesSide = sideMenu.lookup("#btnAjustesSide");
                    if (btnAjustesSide != null) {
                        if (btnAjustesSide instanceof javafx.scene.control.MenuButton) {
                            javafx.scene.control.MenuButton mb = (javafx.scene.control.MenuButton) btnAjustesSide;
                            for (javafx.scene.control.MenuItem mi : mb.getItems()) {
                                if ("miToggleSide".equals(mi.getId())) {
                                    mi.setOnAction(evt -> handleToggleMenu());
                                }
                            }
                        } else if (btnAjustesSide instanceof Button) {
                            ((Button) btnAjustesSide).setOnAction(evt -> selectDashboard());
                        }
                    }
                } catch (Exception ex) {
                    // ignore
                }
                // mostrar de nuevo el texto de bienvenida del header
                if (headerController != null) {
                    headerController.setWelcomeVisible(true);
                }
            }
            isSideMenu = !isSideMenu;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSideMenu() {
        try {
            Node sideMenu = FXMLLoader.load(getClass().getResource("/fxml/NavMenuSide.fxml"));
            root.setLeft(sideMenu);
            // conectar cierre de sesión del side menu
            try {
                if (sideMenu instanceof Region) ((Region) sideMenu).setPrefHeight(Double.MAX_VALUE);
                Node logoutSide = sideMenu.lookup("#btnLogoutSide");
                if (logoutSide instanceof Button) ((Button) logoutSide).setOnAction(evt -> {
                    try { logoutToLogin(); } catch (Exception ex) { ex.printStackTrace(); }
                });
                Node btnDashSide = sideMenu.lookup("#btnDashboardSide");
                if (btnDashSide instanceof Button) ((Button) btnDashSide).setOnAction(evt -> selectDashboard());
                Node btnUsersSide = sideMenu.lookup("#btnUsuariosSide");
                if (btnUsersSide instanceof Button) ((Button) btnUsersSide).setOnAction(evt -> selectUsers());
                Node btnAjustesSide = sideMenu.lookup("#btnAjustesSide");
                if (btnAjustesSide != null) {
                    if (btnAjustesSide instanceof javafx.scene.control.MenuButton) {
                        javafx.scene.control.MenuButton mb = (javafx.scene.control.MenuButton) btnAjustesSide;
                        for (javafx.scene.control.MenuItem mi : mb.getItems()) {
                            if ("miToggleSide".equals(mi.getId())) {
                                mi.setOnAction(evt -> handleToggleMenu());
                            }
                        }
                    } else if (btnAjustesSide instanceof Button) {
                        ((Button) btnAjustesSide).setOnAction(evt -> handleToggleMenu());
                    }
                }
            } catch (Exception ex) {
                // ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logoutToLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Parent loginRoot = loader.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(loginRoot));
        stage.setTitle("Ares Software - Login");
        // al volver al login, restaurar tamaño de ventana a compacto
        try {
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.centerOnScreen();
        } catch (Exception ex) {
            // ignore
        }
    }

    private void loadUsers() {
        try {
            if (userDAO != null && tblUsers != null) {
                // configurar columnas (si no están configuradas ya)
                try {
                    if (colId != null) colId.setCellValueFactory(cell -> cell.getValue().idProperty());
                    if (colNombre != null) colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
                    if (colUsuario != null) colUsuario.setCellValueFactory(cell -> cell.getValue().usuarioProperty());
                    if (colRol != null) colRol.setCellValueFactory(cell -> cell.getValue().idRolProperty());
                    if (colEstado != null) colEstado.setCellValueFactory(cell -> cell.getValue().estadoProperty());
                } catch (Exception ex) {
                    // ignore
                }

                // Añadir columna de acción (Habilitar/Inhabilitar)
                try {
                    javafx.scene.control.TableColumn<com.aressoftware.model.security.User, Void> colAction =
                            new javafx.scene.control.TableColumn<>("Acción");
                    colAction.setPrefWidth(120);
                        colAction.setCellFactory(param -> new javafx.scene.control.TableCell<com.aressoftware.model.security.User, Void>() {
                        private final javafx.scene.control.Button actionBtn = new javafx.scene.control.Button();
                        {
                            actionBtn.getStyleClass().add("table-action-button");
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                return;
                            }
                            User u = getTableView().getItems().get(getIndex());
                            if (u == null) {
                                setGraphic(null);
                                return;
                            }
                            boolean activo = "Activo".equalsIgnoreCase(u.getEstado());
                            actionBtn.setText(activo ? "Inhabilitar" : "Habilitar");
                            actionBtn.setOnAction(evt -> {
                                try {
                                    String nuevo = activo ? "Inactivo" : "Activo";
                                    boolean ok = userDAO.cambiarEstado(u.getId(), nuevo);
                                    if (ok) {
                                        u.setEstado(nuevo);
                                        // refrescar tabla y dashboard
                                        tblUsers.refresh();
                                        if (dashboardController != null) dashboardController.loadData();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                            setGraphic(actionBtn);
                        }
                    });
                    // Añadir la columna al final si no existe
                    boolean exists = tblUsers.getColumns().stream().anyMatch(c -> "Acción".equals(c.getText()));
                    if (!exists) tblUsers.getColumns().add(colAction);
                } catch (Exception ex) {
                    // ignore
                }

                List<User> usuarios = userDAO.findAll();
                setUsers(usuarios);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectDashboard() {
        if (tabPane != null) tabPane.getSelectionModel().select(0);
    }

    private void selectUsers() {
        if (tabPane != null) tabPane.getSelectionModel().select(1);
    }
}