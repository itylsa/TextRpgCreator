/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.ui;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.scenario.Arrow;
import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author eiko1
 */
public class MouseController {

    Node n;

    private static final double MAX_SCALE = 1.0d;
    private static final double MIN_SCALE = 0.001d;

    private DragContext sceneDragContext = new DragContext();

    private EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent e) {
            if(e.getButton() == MouseButton.PRIMARY && e.getTarget().toString().contains("scalingPane") && e.getClickCount() == 2) {
                App.getWindowController().addDragableScenario(e.getX(), e.getY(), null);
            }
            if(e.getButton() == MouseButton.PRIMARY && e.getTarget().toString().contains("coverPane") && (e.getClickCount() == 2 || e.getClickCount() == 4)) {
                Pane p = (Pane) e.getTarget();
                Scenario scenario = App.findScenario((AnchorPane) p.getParent());
                App.openEditor("ScenarioEditor", scenario, null);
            }
            if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && (e.getTarget().toString().contains("Line") || e.getTarget().toString().contains("Circle"))) {
                Node n = (Node) e.getTarget();
                Choice choice = App.findScenario(n);
                App.openEditor("ChoiceEditor", null, choice);
            }
            if(e.getButton() == MouseButton.PRIMARY && !e.getTarget().toString().contains("scalingPane")) {
                if(e.getTarget().toString().contains("Line") || e.getTarget().toString().contains("Circle")) {
                    App.getScenarios().forEach((t) -> {
                        t.getIncomingChoices().forEach((b) -> {
                            if(b.getChoiceArrow().getLine() == e.getTarget() || b.getChoiceArrow().getCircle() == e.getTarget()) {
                                n = b.getChoiceArrow();
                            }
                        });
                    });
                } else {
                    n = (Node) e.getTarget();
                }
                App.addMarkedNode(n, App.getShortcutController().isIsShiftPressed());
                n = null;
            }
            if(e.getButton() == MouseButton.PRIMARY && e.getTarget().toString().contains("scalingPane") && !App.getShortcutController().isIsShiftPressed()) {
                App.clearMarkedNodes();
            }
        }
    };

    private EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent e) {
            if(!e.isMiddleButtonDown()) {
                return;
            }

            sceneDragContext.mouseAnchorX = e.getSceneX();
            sceneDragContext.mouseAnchorY = e.getSceneY();

            sceneDragContext.translateAnchorX = App.getWindowController().getScalingPane().getTranslateX();
            sceneDragContext.translateAnchorY = App.getWindowController().getScalingPane().getTranslateY();
        }
    };

    private EventHandler<MouseEvent> mouseDragged = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent e) {
            if(e.isPrimaryButtonDown()) {
            }
            if(!e.isMiddleButtonDown()) {
                return;
            }

            App.getWindowController().getScalingPane().setTranslateX(sceneDragContext.translateAnchorX + e.getSceneX() - sceneDragContext.mouseAnchorX);
            App.getWindowController().getScalingPane().setTranslateY(sceneDragContext.translateAnchorY + e.getSceneY() - sceneDragContext.mouseAnchorY);

            e.consume();
        }
    };

    private EventHandler<ScrollEvent> mouseScrolled = new EventHandler<ScrollEvent>() {
        public void handle(ScrollEvent event) {
//            if(event.getDeltaY() > 0) {
//                App.getWindowController().getScalingPane().setScaleX(App.getWindowController().getScalingPane().getScaleX() + 0.15);
//                App.getWindowController().getScalingPane().setScaleY(App.getWindowController().getScalingPane().getScaleY() + 0.15);
//            } else if(App.getWindowController().getScalingPane().getScaleX() > 0.2) {
//                App.getWindowController().getScalingPane().setScaleX(App.getWindowController().getScalingPane().getScaleX() - 0.15);
//                App.getWindowController().getScalingPane().setScaleY(App.getWindowController().getScalingPane().getScaleY() - 0.15);
//            }

            double delta = 1.2;

            double scaleX = App.getWindowController().getScalingPane().getScaleX(); // currently we only use Y, same value is used for X
            double scaleY = App.getWindowController().getScalingPane().getScaleY(); // currently we only use Y, same value is used for X
            double oldScaleX = scaleX;
            double oldScaleY = scaleY;

            if(event.getDeltaY() < 0) {
                scaleX /= delta;
                scaleY /= delta;
            } else {
                scaleX *= delta;
                scaleY *= delta;
            }

            scaleX = clamp(scaleX, MIN_SCALE, MAX_SCALE);
            scaleY = clamp(scaleY, MIN_SCALE, MAX_SCALE);

            double f = (scaleX / oldScaleX) - 1;
            double d = (scaleY / oldScaleY) - 1;

            double dx = (event.getSceneX() - (App.getWindowController().getScalingPane().getBoundsInParent().getWidth() / 2 + App.getWindowController().getScalingPane().getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (App.getWindowController().getScalingPane().getBoundsInParent().getHeight() / 2 + App.getWindowController().getScalingPane().getBoundsInParent().getMinY()));

            App.getWindowController().getScalingPane().setScaleX(scaleX);
            App.getWindowController().getScalingPane().setScaleY(scaleY);

            // note: pivot value must be untransformed, i. e. without scaling
            setPivot(f * dx, d * dy);

            event.consume();
        }
    };

    public void setPivot(double x, double y) {
        App.getWindowController().getScalingPane().setTranslateX(App.getWindowController().getScalingPane().getTranslateX() - x);
        App.getWindowController().getScalingPane().setTranslateY(App.getWindowController().getScalingPane().getTranslateY() - y);
    }

    public static double clamp(double value, double min, double max) {

        if(Double.compare(value, min) < 0) {
            return min;
        }

        if(Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    class DragContext {

        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;

    }

    public EventHandler<MouseEvent> getMouseClicked() {
        return mouseClicked;
    }

    public EventHandler<ScrollEvent> getMouseScrolled() {
        return mouseScrolled;
    }

    public EventHandler<MouseEvent> getMousePressed() {
        return mousePressed;
    }

    public EventHandler<MouseEvent> getMouseDragged() {
        return mouseDragged;
    }
}
