package com.heiko.textrpgcreator.controller.node;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.heiko.textrpgcreator.App;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class InfoController extends Controller implements Initializable {

    @FXML
    Label infoText;
    @FXML
    AnchorPane pane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setInfoController(this);
    }

    public void setInfoText(String text) {
        infoText.setText(text);
    }

    public Label getInfoText() {
        return infoText;
    }

    public AnchorPane getPane() {
        return pane;
    }
}
