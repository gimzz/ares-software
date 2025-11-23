package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.User;

import java.util.List;

public class UsuariosController {

    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, Number> colId;
    @FXML private TableColumn<User, String> colNombre;
    @FXML private TableColumn<User, String> colUsuario;
    @FXML private TableColumn<User, Number> colRol;
    @FXML private TableColumn<User, String> colEstado;

    private UserDAO userDAO;
    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        configureUserTable();
        addActionColumn();
        refreshUserData();
    }

    private void configureUserTable() {
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colUsuario.setCellValueFactory(cell -> cell.getValue().usuarioProperty());
        colRol.setCellValueFactory(cell -> cell.getValue().idRolProperty());
        colEstado.setCellValueFactory(cell -> cell.getValue().estadoProperty());
        
        // --- Solución al error FXML y Responsive ---
        // Configurar la política de redimensionamiento para que las columnas se ajusten al ancho total.
        tblUsers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // --- Mejoras Estéticas (Centrado) ---
        colId.getStyleClass().add("table-cell-center"); 
        colRol.getStyleClass().add("table-cell-center");
        // Las propiedades min/max width para el responsive se definen en el FXML

        // Evitar reordenar columnas
        for (TableColumn<?, ?> column : tblUsers.getColumns()) {
            column.setReorderable(false);
        }
    }

    private void addActionColumn() {
        TableColumn<User, Void> colAction = new TableColumn<>("Acción");
        
        // --- Configuración Responsive para la Columna de Acción ---
        colAction.setPrefWidth(120);
        colAction.setMinWidth(100); 
        colAction.setMaxWidth(150); 

        // Aplicar clase CSS para centrar los botones
        colAction.getStyleClass().add("table-cell-center"); 

        colAction.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button actionBtn = new Button();

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
                actionBtn.getStyleClass().removeAll("btn-enable", "btn-disable");
                actionBtn.getStyleClass().add(activo ? "btn-disable" : "btn-enable");

                actionBtn.setOnAction(evt -> {
                    try {
                        String nuevo = activo ? "Inactivo" : "Activo";
                        boolean ok = userDAO != null && userDAO.cambiarEstado(u.getId(), nuevo);

                        if (ok) {
                            u.setEstado(nuevo);
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

        // Asegurar que la columna de acción se añada solo una vez
        boolean exists = tblUsers.getColumns().stream().anyMatch(c -> "Acción".equals(c.getText()));
        if (!exists) tblUsers.getColumns().add(colAction);
    }

    private void refreshUserData() {
        try {
            List<User> usuarios = userDAO.findAll();
            ObservableList<User> data = FXCollections.observableArrayList(usuarios);
            tblUsers.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}