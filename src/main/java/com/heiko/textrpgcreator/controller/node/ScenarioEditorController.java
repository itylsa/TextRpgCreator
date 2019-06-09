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
    private TextArea scenarioTags;
    @FXML
    private TextArea scenarioText;
    @FXML
    private Label tagsLabel;
    @FXML
    private Label textLabel;

    private Scenario scenario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setScenarioEditorController(this);
        scenarioTags.requestFocus();
        scenarioText.setStyle("-fx-text-fill: white;");
        scenarioTags.setStyle("-fx-text-fill: white;");
    }

    public void openScenarioEditor(Scenario scenario) {
        this.scenario = scenario;
        scenarioTags.setText(scenario.getTags());
        scenarioText.setText(scenario.getBody());
    }

    public void closeScenarioEditor() {
        scenario.setTags(scenarioTags.getText());
        scenario.setBody(scenarioText.getText());
        scenario.getDragableScenarioController().setBodyAndTags(scenarioTags.getText(), scenarioText.getText());
        scenario.getDragableScenarioController().colorIt();
        scenario = null;
        scenarioTags.setText("");
        scenarioText.setText("");
        App.setCurrentEdit(null);
        App.closeEditor();
    }

    public Parent getPane() {
        return pane;
    }

    public VBox getBox() {
        return box;
    }

    public TextArea getScenarioTags() {
        return scenarioTags;
    }

    public TextArea getScenarioText() {
        return scenarioText;
    }

    public Label getTagsLabel() {
        return tagsLabel;
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
