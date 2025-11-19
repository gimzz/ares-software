package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HeaderController {

	@FXML private Label lblWelcomeHeader;

	private HomeController homeController;

	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
		// no hay botón de toggle en el header; el toggle se realizará desde 'Ajustes' en los menús
	}

	public void setWelcomeText(String text) {
		if (lblWelcomeHeader != null) lblWelcomeHeader.setText(text);
	}



	public void setWelcomeVisible(boolean visible) {
		if (lblWelcomeHeader != null) lblWelcomeHeader.setVisible(visible);
	}
}
