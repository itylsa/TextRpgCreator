/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.scenario.Scenario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class DeleteChoiceController implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private Label label;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setDeleteChoiceController(this);
    }

    public void openDeleteChoice() {
        label.setText("Do you really want to delete " + App.getScenariosToDelete().size() + " Scenarios and " + App.getChoicesToDelete().size() + " Choices to delete?");
    }

    public void closeDeleteChoice() {
//        scenario.setTitle(scenarioTitle.getText());
//        scenario.setBody(scenarioText.getText());
//        scenario.getDragableScenarioController().setBodyAndTitle(scenarioTitle.getText(), scenarioText.getText());
//        scenario = null;
//        scenarioTitle.setText("");
//        scenarioText.setText("");
        App.setCurrentEdit(null);
        App.closeDeleteChoice();
    }

    @FXML
    private void doYes() {
        System.out.println("yes");
    }

    @FXML
    private void doNo() {
        System.out.println("no");
    }

    public AnchorPane getAnchorParentPane() {
        return (AnchorPane) pane;
    }

    public Parent getPane() {
        return pane;
    }
}
