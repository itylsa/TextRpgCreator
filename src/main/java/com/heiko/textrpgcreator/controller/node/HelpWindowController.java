/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class HelpWindowController implements Initializable {

    @FXML
    AnchorPane pane;
    @FXML
    TextArea text;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setHelpWindowController(this);
        text.setStyle("-fx-text-fill: white;");
    }

    public AnchorPane getPane() {
        return pane;
    }

    public TextArea getText() {
        return text;
    }
}
