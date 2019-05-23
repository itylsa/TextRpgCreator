/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.scenario;

import com.heiko.textrpgcreator.App;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author eiko1
 */
public class Arrow extends Line {

    private Line line;

    private Line fillLine;

    private Circle circle;

    private double radius = 20;

    private double width = 8;

    private AnchorPane startPane;

    private AnchorPane endPane;

    private Color color = Color.BLACK;

    private boolean hasText = false;

    public Arrow(AnchorPane startPane, AnchorPane endPane, double startX, double startY, double endX, double endY) {
        line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(width);
        circle = new Circle(endX, endY, radius);
        setColor(color);
        App.getWindowController().getScalingPane().getChildren().add(line);
        App.getWindowController().getScalingPane().getChildren().add(circle);
        Scenario start = App.findScenario(startPane);
        Scenario end = App.findScenario(endPane);
        start.getOutgoingChoices().add(new Choice("", start, end, this));
        end.getIncomingChoices().add(new Choice("", start, end, this));
        this.startPane = startPane;
        this.endPane = endPane;
    }

    public void setMarked() {
        setColor(Color.AQUA);
    }

    public void removeMarked() {
        if(hasText) {
            setColor(Color.GREEN);
        } else {
            setColor(Color.BLACK);
        }
    }

    public void moveEnd(double endX, double endY) {
        setEndLine(endX, endY);
        setCircleCenter(endX, endY);
    }

    public void moveStart(double startX, double startY) {
        setStartLine(startX, startY);
    }

    public void setStartLine(double startX, double startY) {
        line.setStartX(startX);
        line.setStartY(startY);
        if(fillLine != null) {
            fillLine.setStartX(startX);
            fillLine.setStartY(startY);
            fillLine.toFront();
        }
    }

    public void setEndLine(double endX, double endY) {
        line.setEndX(endX);
        line.setEndY(endY);
        if(fillLine != null) {
            fillLine.setEndX(endX);
            fillLine.setEndY(endY);
            fillLine.toFront();
        }
    }

    public void setCircleCenter(double centerX, double centerY) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
    }

    public Line getLine() {
        return line;
    }

    public Circle getCircle() {
        return circle;
    }

    public double getRadius() {
        return radius;
    }

    public double getWidth() {
        return width;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public AnchorPane getStartPane() {
        return startPane;
    }

    public void setStartPane(AnchorPane startPane) {
        this.startPane = startPane;
    }

    public AnchorPane getEndPane() {
        return endPane;
    }

    public void setEndPane(AnchorPane endPane) {
        this.endPane = endPane;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        line.setStroke(color);
        circle.setStroke(color);
        circle.setFill(color);
    }

    public boolean isHasText() {
        return hasText;
    }

    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }
}
