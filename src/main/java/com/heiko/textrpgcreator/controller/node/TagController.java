/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class TagController extends Controller implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private Label groupLabel;
    @FXML
    private Label tagLabel;
    @FXML
    private Label valueLabel;

    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void deleteTag() {
        
    }

    public Parent getPane() {
        return pane;
    }

    public AnchorPane getAnchorParentPane() {
        return (AnchorPane) pane;
    }

    public Label getGroupLabel() {
        return groupLabel;
    }

    public Label getTagLabel() {
        return tagLabel;
    }

    public Label getValueLabel() {
        return valueLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel.setText(groupLabel);
    }

    public void setTagLabel(String tagLabel) {
        this.tagLabel.setText(tagLabel);
    }

    public void setValueLabel(String valueLabel) {
        this.valueLabel.setText(valueLabel);
    }
}
