package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.User;

import java.util.List;

public class DashboardController {

    @FXML private ProgressBar progressUsuarios;
    @FXML private ProgressBar progressCarga;
    @FXML private PieChart pieChartDashboard;
    @FXML private TableView<User> tblActivity;
    @FXML private TableColumn<User, String> colEvento;
    @FXML private TableColumn<User, String> colUsuario;
    @FXML private TableColumn<User, String> colFecha;

    private UserDAO userDAO;

    public void setUserDao(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void loadData() {
        if (userDAO == null) return;
        try {
            List<User> usuarios = userDAO.findAll();
            refreshPieChartFromUsers(usuarios);
            // Aquí podrías cargar actividad reciente si la tienes en tu modelo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshPieChartFromUsers(List<User> users) {
        long activos = users.stream().filter(u -> "Activo".equalsIgnoreCase(u.getEstado())).count();
        long inactivos = users.stream().filter(u -> "Inactivo".equalsIgnoreCase(u.getEstado())).count();

        pieChartDashboard.setData(FXCollections.observableArrayList(
                new PieChart.Data("Activos", activos),
                new PieChart.Data("Inactivos", inactivos)
        ));
    }
}