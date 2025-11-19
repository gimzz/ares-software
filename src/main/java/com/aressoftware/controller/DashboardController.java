package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import com.aressoftware.dao.UserDAO;
import com.aressoftware.model.security.User;
import javafx.collections.FXCollections;

import java.util.List;

public class DashboardController {

	@FXML private PieChart pieChartDashboard;
	@FXML private ProgressBar progressUsuarios;
	@FXML private ProgressBar progressCarga;
	@FXML private TableView<?> tblActivity;

	private UserDAO userDao;

	public void setUserDao(UserDAO dao) {
		this.userDao = dao;
	}

	public void loadData() {
		// cargar datos simples si el DAO está presente
		if (userDao != null && pieChartDashboard != null) {
			try {
				List<User> users = userDao.findAll();
				long activos = users.stream().filter(u -> "Activo".equalsIgnoreCase(u.getEstado())).count();
				long inactivos = users.size() - activos;
				pieChartDashboard.setData(FXCollections.observableArrayList(
						new PieChart.Data("Activos", activos),
						new PieChart.Data("Inactivos", inactivos)
				));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
