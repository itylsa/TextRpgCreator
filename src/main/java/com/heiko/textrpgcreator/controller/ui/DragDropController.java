/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.ui;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.controller.node.DragableScenarioController;
import com.heiko.textrpgcreator.scenario.Arrow;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author eiko1
 */
public class DragDropController {

    private EventHandler<MouseEvent> dragDetected;

    private EventHandler<DragEvent> dragOver;

    private EventHandler<DragEvent> dragEntered;

    private EventHandler<DragEvent> dragExited;

    private EventHandler<DragEvent> dragDropped;

    private EventHandler<DragEvent> dragDone;

    private AnchorPane currentPane;

    private List<Arrow> currentOutgoingArrows = new ArrayList<Arrow>();

    private List<Arrow> currentIncomingArrows = new ArrayList<Arrow>();

    private Point2D startPoint = Point2D.ZERO;

    private Point2D endPoint = Point2D.ZERO;

    public DragDropController() {
        setDragDetected();
        setDragOver();
        setDragEntered();
        setDragExited();
        setDragDropped();
        setDragDone();
    }

    private void moveNode(Object pane, double x, double y) {
        AnchorPane aPane = (AnchorPane) pane;
        aPane.setLayoutX(x - aPane.getPrefWidth() / 2);
        aPane.setLayoutY(y - aPane.getPrefHeight() / 2);
    }

    public void setDragDetected() {
        dragDetected = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                ClipboardContent content = new ClipboardContent();
                Dragboard db = null;
                if(e.getButton() == MouseButton.PRIMARY) {
                    AnchorPane aPane = (AnchorPane) e.getSource();
                    db = aPane.startDragAndDrop(TransferMode.ANY);
                    content.putString("DRAG ME");
                    App.findScenario(aPane);
                    App.getCurrentEdit().getOutgoingChoices().forEach((t) -> {
                        currentOutgoingArrows.add(t.getChoiceArrow());
                    });
                    App.getCurrentEdit().getIncomingChoices().forEach((t) -> {
                        currentIncomingArrows.add(t.getChoiceArrow());
                    });
                }
                if(e.getButton() == MouseButton.SECONDARY) {
                    if(e.getSource().getClass().toString().contains("AnchorPane")) {
                        AnchorPane aPane = (AnchorPane) e.getSource();
                        db = aPane.startDragAndDrop(TransferMode.ANY);
                        content.putString("new Arrow");
                        currentPane = aPane;
                        Pane ccp = (Pane) currentPane.getChildren().get(1);
                        ccp.setStyle("-fx-border-color: lightgreen; -fx-border-width: 5px");
                    }
                }
                if(db != null) {
                    db.setContent(content);
                }
                e.consume();
            }
        };
    }

    public void setDragOver() {
        dragOver = new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {
                Dragboard db = e.getDragboard();
                if(db.hasString() && db.getString().equals("DRAG ME")) {
                    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    if(e.getSource().toString().contains("AnchorPane")) {
                        AnchorPane aPane = (AnchorPane) e.getSource();
                        moveNode(e.getGestureSource(), e.getX() + aPane.getLayoutX(), e.getY() + aPane.getLayoutY());
                        moveArrows(e);
                    } else {
                        moveNode(e.getGestureSource(), e.getX(), e.getY());
                        moveArrows(e);
                    }
                }
                if(db.hasString() && db.getString().equals("new Arrow") && e.getSource().toString().contains("scalingPane")) {
                    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                if(db.hasString() && db.getString().equals("new Arrow") && e.getSource().toString().contains("AnchorPane") && currentPane != e.getSource()) {
                    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                e.consume();
            }
        };
    }

    private void moveArrows(DragEvent e) {
        if(currentOutgoingArrows.size() > 0) {
            currentOutgoingArrows.forEach((t) -> {
                whichSide(t.getStartPane(), t.getEndPane());
                t.moveStart(startPoint.getX(), startPoint.getY());
                t.moveEnd(endPoint.getX(), endPoint.getY());
                startPoint = Point2D.ZERO;
                endPoint = Point2D.ZERO;
            });
        }
        if(currentIncomingArrows.size() > 0) {
            currentIncomingArrows.forEach((t) -> {
                whichSide(t.getStartPane(), t.getEndPane());
                t.moveStart(startPoint.getX(), startPoint.getY());
                t.moveEnd(endPoint.getX(), endPoint.getY());
                startPoint = Point2D.ZERO;
                endPoint = Point2D.ZERO;
            });
        }
    }

    public void setDragEntered() {
        dragEntered = new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {
                Dragboard db = e.getDragboard();
                if(db.hasString() && db.getString().equals("new Arrow") && e.getSource().toString().contains("AnchorPane") && e.getSource() != currentPane) {
                    AnchorPane p = (AnchorPane) e.getSource();
                    Pane cp = (Pane) p.getChildren().get(1);
                    cp.setStyle("-fx-border-color: lightgreen; -fx-border-width: 5px");
                }
            }
        };
    }

    public void setDragExited() {
        dragExited = new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {
                Dragboard db = e.getDragboard();
                if(db.hasString() && db.getString().equals("new Arrow") && e.getSource().toString().contains("AnchorPane") && e.getSource() != currentPane) {
                    AnchorPane p = (AnchorPane) e.getSource();
                    Pane cp = (Pane) p.getChildren().get(1);
                    cp.setStyle("-fx-border-color: black; -fx-border-width: 1px");
                }
            }
        };
    }

    public void setDragDropped() {
        dragDropped = new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {
                Dragboard db = e.getDragboard();
                boolean success = false;
                if(db.hasString() && db.getString().equals("DRAG ME")) {
                    success = true;
                }

                AnchorPane targetPane = null;
                if(db.hasString() && db.getString().equals("new Arrow") && e.getGestureTarget().toString().contains("scalingPane")) {
                    App.getWindowController().addDragableScenario(e.getX(), e.getY(), null);
                    targetPane = (AnchorPane) App.getWindowController().getScalingPane().getChildren().get(App.getWindowController().getScalingPane().getChildren().size() - 1);
                }
                if(db.hasString() && db.getString().equals("new Arrow") && e.getGestureTarget().toString().contains("AnchorPane")) {
                    targetPane = (AnchorPane) e.getGestureTarget();
                }

                if(targetPane != null) {
                    whichSide(currentPane, targetPane);
                    new Arrow(currentPane, targetPane, startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                }

                Pane ccp = (Pane) currentPane.getChildren().get(1);
                ccp.setStyle("-fx-border-color: black; -fx-border-width: 1px");

                currentIncomingArrows.clear();
                currentOutgoingArrows.clear();
                startPoint = Point2D.ZERO;
                endPoint = Point2D.ZERO;
                e.setDropCompleted(success);
                e.consume();
            }
        };
    }

    public void setDragDone() {
        dragDone = new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {

            }
        };
    }

    private void whichSide(AnchorPane currentPane, AnchorPane targetPane) {
        double differenceX = currentPane.getLayoutX() - targetPane.getLayoutX();
        double differenceY = currentPane.getLayoutY() - targetPane.getLayoutY();
        if(Math.abs(differenceX) > Math.abs(differenceY)) {
            if(differenceX < 0) {
                startPoint = new Point2D(currentPane.getLayoutX() + currentPane.getPrefWidth(), currentPane.getLayoutY() + currentPane.getPrefHeight() / 2);
                endPoint = new Point2D(targetPane.getLayoutX(), targetPane.getLayoutY() + targetPane.getPrefHeight() / 2);
            } else {
                startPoint = new Point2D(currentPane.getLayoutX(), currentPane.getLayoutY() + currentPane.getPrefHeight() / 2);
                endPoint = new Point2D(targetPane.getLayoutX() + targetPane.getPrefWidth(), targetPane.getLayoutY() + targetPane.getPrefHeight() / 2);
            }
        } else {
            if(differenceY < 0) {
                startPoint = new Point2D(currentPane.getLayoutX() + currentPane.getPrefWidth() / 2, currentPane.getLayoutY() + currentPane.getPrefHeight());
                endPoint = new Point2D(targetPane.getLayoutX() + targetPane.getPrefWidth() / 2, targetPane.getLayoutY());
            } else {
                startPoint = new Point2D(currentPane.getLayoutX() + currentPane.getPrefWidth() / 2, currentPane.getLayoutY());
                endPoint = new Point2D(targetPane.getLayoutX() + targetPane.getPrefWidth() / 2, targetPane.getLayoutY() + targetPane.getPrefHeight());
            }
        }
    }

    public EventHandler<MouseEvent> getDragDetected() {
        return dragDetected;
    }

    public EventHandler<DragEvent> getDragOver() {
        return dragOver;
    }

    public EventHandler<DragEvent> getDragEntered() {
        return dragEntered;
    }

    public EventHandler<DragEvent> getDragExited() {
        return dragExited;
    }

    public EventHandler<DragEvent> getDragDropped() {
        return dragDropped;
    }

    public EventHandler<DragEvent> getDragDone() {
        return dragDone;
    }
}
