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
    private TextArea textTitle;
    @FXML
    private TextArea textBody;
    @FXML
    private Pane coverPane;
    
    private final BooleanProperty firstTime = new SimpleBooleanProperty(true);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getDragableScenarioControllers().add(this);
        removeFocus();
        addDragDropEvents();
    }
    
    public void addDragDropEvents() {
        pane.setOnDragDetected(App.getDragDropController().getDragDetected());
        pane.setOnDragOver(App.getDragDropController().getDragOver());
        pane.setOnDragEntered(App.getDragDropController().getDragEntered());
        pane.setOnDragExited(App.getDragDropController().getDragExited());
    }

    public void setTextInBody(String text) {
        if(text.length() > 65) {
            text = text.substring(0, 65);
            text = text + " ...";
        }
        textBody.setText(text);
    }

    public void setTextInTitle(String text) {
        if(text.length() > 11) {
            text = text.substring(0, 11);
            text = text + " ...";
        }
        textTitle.setText(text);
    }

    public void setBodyAndTitle(String body, String title) {
        setTextInBody(body);
        setTextInTitle(title);
    }

    public void setPosition(double x, double y) {
        pane.setLayoutX(x - getAnchorParentPane().getPrefWidth() / 2);
        pane.setLayoutY(y - getAnchorParentPane().getPrefHeight() / 2);
    }

    private void removeFocus() {
        textTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
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

    public TextArea getTextTitle() {
        return textTitle;
    }

    public TextArea getTextBody() {
        return textBody;
    }

    public Pane getCoverPane() {
        return coverPane;
    }
}
