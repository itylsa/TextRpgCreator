/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class ChoiceEditorController implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private VBox box;
    @FXML
    private TextArea choiceText;
    @FXML
    private TextArea choiceTags;
    @FXML
    private Label textLabel;

    private Choice choice;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setChoiceEditorController(this);
        choiceText.requestFocus();
        choiceText.setStyle("-fx-text-fill: white;");
        choiceTags.setStyle("-fx-text-fill: white;");
    }

    public void openChoiceEditor(Choice choice) {
        this.choice = choice;
        choiceTags.setText(choice.getTags());
        choiceText.setText(choice.getText());
    }

    public void closeChoiceEditor() {
        choice.setText(choiceText.getText());
        choice.setTags(choiceTags.getText());
        choice.getChoiceArrow().colorArrow();
        choice = null;
        choiceText.setText("");
        App.setCurrentChoice(null);
        App.closeEditor();
    }

    public Parent getPane() {
        return pane;
    }

    public VBox getBox() {
        return box;
    }

    public TextArea getChoiceText() {
        return choiceText;
    }

    public Label getTextLabel() {
        return textLabel;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public TextArea getChoiceTags() {
        return choiceTags;
    }
}
