/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.ui;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.scenario.Arrow;
import com.heiko.textrpgcreator.scenario.Scenario;
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

    private List<Scenario> markedPaneScenarios = new ArrayList();

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

    public void setDragDetected() {
        dragDetected = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                ClipboardContent content = new ClipboardContent();
                Dragboard db = null;
                if(e.getButton() == MouseButton.PRIMARY) {
                    AnchorPane aPane = (AnchorPane) e.getSource();
                    db = aPane.startDragAndDrop(TransferMode.ANY);
                    content.putString("DRAG ME");
                    currentPane = aPane;
                    if(!App.getMarkedNodes().contains(aPane.getChildren().get(1))) {
                        App.addMarkedNode(aPane.getChildren().get(1), App.getShortcutController().isIsShiftPressed());
                    }
                    if(markedPaneScenarios.size() > 0) {
                        markedPaneScenarios.forEach((t) -> {
                            t.getIncomingChoices().forEach((b) -> {
                                currentIncomingArrows.add(b.getChoiceArrow());
                            });
                            t.getOutgoingChoices().forEach((b) -> {
                                currentOutgoingArrows.add(b.getChoiceArrow());
                            });
                        });
                    }
                }
                if(e.getButton() == MouseButton.SECONDARY) {
                    if(e.getSource().getClass().toString().contains("AnchorPane")) {
                        AnchorPane aPane = (AnchorPane) e.getSource();
                        db = aPane.startDragAndDrop(TransferMode.ANY);
                        content.putString("new Arrow");
                        currentPane = aPane;
                        Pane ccp = (Pane) currentPane.getChildren().get(1);
                        App.clearMarkedNodes();
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
                        markedPaneScenarios.forEach((t) -> {
                            moveNode(t, e.getX() + t.getDragableScenarioController().getAnchorParentPane().getLayoutX(), e.getY() + t.getDragableScenarioController().getAnchorParentPane().getLayoutY());
                        });
                        moveArrows(e);
                    } else {
                        double oldX = currentPane.getLayoutX();
                        double oldY = currentPane.getLayoutY();
                        moveNode(App.findScenario(currentPane), e.getX(), e.getY());
                        markedPaneScenarios.forEach((t) -> {
                            if(currentPane != t.getDragableScenarioController().getAnchorParentPane()) {
                                moveNode(t, t.getDragableScenarioController().getAnchorParentPane().getLayoutX() + currentPane.getLayoutX() - oldX + (t.getDragableScenarioController().getAnchorParentPane().getPrefWidth() / 2),
                                        t.getDragableScenarioController().getAnchorParentPane().getLayoutY() + currentPane.getLayoutY() - oldY + (t.getDragableScenarioController().getAnchorParentPane().getPrefHeight() / 2));
                            }
                        });
                        moveArrows(e);
                    }
                }
                if(db.hasString() && db.getString().equals("new Arrow") && e.getSource().toString().contains("scalingPane")) {
                    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                if(db.hasString() && db.getString().equals("new Arrow") && e.getSource().toString().contains("AnchorPane")) {
                    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                e.consume();
            }
        };
    }

    private void moveNode(Object pane, double x, double y) {
        Scenario s = (Scenario) pane;
        AnchorPane aPane = s.getDragableScenarioController().getAnchorParentPane();
        aPane.setLayoutX(x - aPane.getPrefWidth() / 2);
        aPane.setLayoutY(y - aPane.getPrefHeight() / 2);
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

                if(targetPane != null && !App.arrowAllreadyExists(currentPane, targetPane) && currentPane.getChildren().get(1) != e.getTarget()) {
                    whichSide(currentPane, targetPane);
                    new Arrow(currentPane, targetPane, startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                } else if(currentPane.getChildren().get(1) == e.getTarget()) {
                    System.out.println("Cant go on itself");
                } else {
                    App.setArrowExists(false);
                    System.out.println("Arrow exists");
                    //TODO
                }
                Pane ccp = (Pane) currentPane.getChildren().get(1);
                if(ccp.getStyle().contains("border")) {
                    ccp.setStyle("-fx-border-color: black; -fx-border-width: 1px");
                }

                if(App.getShortcutController().isIsShiftPressed()) {
                    App.getShortcutController().setIsShiftPressed(false);
                }
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

    public List<Scenario> getMarkedPaneScenarios() {
        return markedPaneScenarios;
    }
}
