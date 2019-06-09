/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.scenario.Arrow;
import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class WindowController extends Controller implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private Pane scalingPane;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

    private double size = 100000;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setWindowController(this);
        setMouseListeners();
        addDragDropEvents();
        labelAreaIncrease();
        borderLabelLines();
    }

    public void addDragableScenario(double x, double y, Scenario scenario) {
        DragableScenarioController controller = (DragableScenarioController) App.loadFXMLController("DragableScenario");
        if(scenario == null) {
            App.getScenarios().add(new Scenario(App.getHighestId() + 1, controller.getTextTags().getText(), controller.getTextBody().getText(), controller));
            App.setHighestId(App.getHighestId() + 1);
            controller.setPosition(x, y);
        } else {
            scenario.setDragableScenarioController(controller);
            App.getScenarios().add(scenario);
            controller.setBodyAndTags(scenario.getTags(), scenario.getBody());
            controller.setPositionCorner(x, y);
        }
        scalingPane.getChildren().add(controller.getPane());
    }

    public void addArrow(Scenario start, Scenario end, int id, String tags, String text) {
        App.getDragDropController().whichSide(start.getDragableScenarioController().getAnchorParentPane(), end.getDragableScenarioController().getAnchorParentPane());
        Arrow arrow = new Arrow(
                start.getDragableScenarioController().getAnchorParentPane(),
                end.getDragableScenarioController().getAnchorParentPane(),
                App.getDragDropController().getStartPoint().getX(),
                App.getDragDropController().getStartPoint().getY(),
                App.getDragDropController().getEndPoint().getX(),
                App.getDragDropController().getEndPoint().getY());
        Choice choice = arrow.getChoice();
        choice.setId(id);
        choice.setTags(tags);
        choice.setText(text);
        arrow.colorArrow();
    }

    private void labelAreaIncrease() {
        label1.setLayoutX(-size);
        label1.setLayoutY(-size);
        label1.setText("1111111111");

        label2.setLayoutX(size);
        label2.setLayoutY(-size);
        label2.setText("2222222222");

        label3.setLayoutX(size);
        label3.setLayoutY(size);
        label3.setText("33333333333");

        label4.setLayoutX(-size);
        label4.setLayoutY(size);
        label4.setText("44444444444");
    }

    private void borderLabelLines() {
        Line line = new Line(label1.getLayoutX(), label1.getLayoutY(), label2.getLayoutX(), label2.getLayoutY());
        line.setStrokeWidth(100);
        line.setStroke(Color.RED);
        scalingPane.getChildren().add(line);
        line = new Line(label2.getLayoutX(), label2.getLayoutY(), label3.getLayoutX(), label3.getLayoutY());
        line.setStrokeWidth(100);
        line.setStroke(Color.RED);
        scalingPane.getChildren().add(line);
        line = new Line(label3.getLayoutX(), label3.getLayoutY(), label4.getLayoutX(), label4.getLayoutY());
        line.setStrokeWidth(100);
        line.setStroke(Color.RED);
        scalingPane.getChildren().add(line);
        line = new Line(label4.getLayoutX(), label4.getLayoutY(), label1.getLayoutX(), label1.getLayoutY());
        line.setStrokeWidth(100);
        line.setStroke(Color.RED);
        scalingPane.getChildren().add(line);
    }

    private void addDragDropEvents() {
        scalingPane.setOnDragOver(App.getDragDropController().getDragOver());
        scalingPane.setOnDragDropped(App.getDragDropController().getDragDropped());
    }

    private void setMouseListeners() {
        scalingPane.setOnMouseClicked(App.getMouseController().getMouseClicked());
        scalingPane.setOnScroll(App.getMouseController().getMouseScrolled());
    }

    public Parent getPane() {
        return pane;
    }
    
    public AnchorPane getAnchorPane() {
        return (AnchorPane) pane;
    }

    public Pane getScalingPane() {
        return scalingPane;
    }

}
