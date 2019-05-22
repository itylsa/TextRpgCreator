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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class ScenarioEditorController implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private VBox box;
    @FXML
    private TextArea scenarioTitle;
    @FXML
    private TextArea scenarioText;
    @FXML
    private Label titleLabel;
    @FXML
    private Label textLabel;

    private Scenario scenario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setScenarioEditorController(this);
        scenarioTitle.requestFocus();
    }

    public void openScenarioEditor(Scenario scenario) {
        this.scenario = scenario;
        scenarioTitle.setText(scenario.getTitle());
        scenarioText.setText(scenario.getBody());
    }

    public void closeScenarioEditor() {
        scenario.setTitle(scenarioTitle.getText());
        scenario.setBody(scenarioText.getText());
        scenario.getDragableScenarioController().setBodyAndTitle(scenarioTitle.getText(), scenarioText.getText());
        scenario = null;
        scenarioTitle.setText("");
        scenarioText.setText("");
        App.setCurrentEdit(null);
    }

    public Parent getPane() {
        return pane;
    }

    public VBox getBox() {
        return box;
    }

    public TextArea getScenarioTitle() {
        return scenarioTitle;
    }

    public TextArea getScenarioText() {
        return scenarioText;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getTextLabel() {
        return textLabel;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
}
