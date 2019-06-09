/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class DragableScenarioController extends Controller implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private TextArea textTags;
    @FXML
    private TextArea textBody;
    @FXML
    private Pane coverPane;

    private boolean marked = false;

    private final BooleanProperty firstTime = new SimpleBooleanProperty(true);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getDragableScenarioControllers().add(this);
        removeFocus();
        addDragDropEvents();
    }

    public void setMarked() {
        marked = true;
        coverPane.setStyle("-fx-background-color: blue; -fx-opacity: 0.5");
    }

    public void removeMarked() {
        marked = false;
        coverPane.setStyle("-fx-opacity: 0.0");
    }

    public void addDragDropEvents() {
        pane.setOnDragDetected(App.getDragDropController().getDragDetected());
        pane.setOnDragOver(App.getDragDropController().getDragOver());
        pane.setOnDragEntered(App.getDragDropController().getDragEntered());
        pane.setOnDragExited(App.getDragDropController().getDragExited());
    }

    public void setTextInBody(String text) {
        textBody.setText(text);
    }

    public void setTextInTags(String text) {
        textTags.setText(text);
    }

    public void setBodyAndTags(String tags, String body) {
        setTextInBody(body);
        setTextInTags(tags);
    }

    public void setPosition(double x, double y) {
        pane.setLayoutX(x - getAnchorParentPane().getPrefWidth() / 2);
        pane.setLayoutY(y - getAnchorParentPane().getPrefHeight() / 2);
    }

    public void setPositionCorner(double x, double y) {
        pane.setLayoutX(x);
        pane.setLayoutY(y);
    }

    private void removeFocus() {
        textTags.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && firstTime.get()) {
                pane.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    public Parent getPane() {
        return pane;
    }

    public AnchorPane getAnchorParentPane() {
        return (AnchorPane) pane;
    }

    public TextArea getTextTags() {
        return textTags;
    }

    public TextArea getTextBody() {
        return textBody;
    }

    public Pane getCoverPane() {
        return coverPane;
    }
}
