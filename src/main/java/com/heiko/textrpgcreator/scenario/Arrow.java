/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.scenario;

import com.heiko.textrpgcreator.App;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author eiko1
 */
public class Arrow extends Line {

    private Line line;

    private Circle circle;

    private double radius = 15;

    private double width = 8;

    private AnchorPane startPane;

    private AnchorPane endPane;

    private Color color = Color.BLACK;

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

    public void moveEnd(double endX, double endY) {
        setEndLine(endX, endY);
        setCircleCenter(endX, endY);
    }

    public void moveStart(double startX, double startY) {
        setStartLine(startX, startY);
    }

    public void setEndLine(double endX, double endY) {
        line.setEndX(endX);
        line.setEndY(endY);
    }

    public void setStartLine(double startX, double startY) {
        line.setStartX(startX);
        line.setStartY(startY);
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
}
