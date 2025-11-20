package com.aressoftware.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HeaderController {

    @FXML private Label lblWelcomeHeader;
    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void setWelcomeText(String text) {
        if (lblWelcomeHeader != null) lblWelcomeHeader.setText(text);
    }

    public void setWelcomeVisible(boolean visible) {
        if (lblWelcomeHeader != null) lblWelcomeHeader.setVisible(visible);
    }

    public Label getWelcomeLabel() {
        return lblWelcomeHeader;
    }
}