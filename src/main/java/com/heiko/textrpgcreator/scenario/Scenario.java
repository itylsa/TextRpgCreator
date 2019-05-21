/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.scenario;

import com.heiko.textrpgcreator.controller.node.DragableScenarioController;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eiko1
 */
public class Scenario {

    private String title;

    private String body;

    private List<Choice> outgoingChoices = new ArrayList<Choice>();

    private List<Choice> incomingChoices = new ArrayList<Choice>();

    private DragableScenarioController dragableScenarioController;

    public Scenario(String title, String body, DragableScenarioController dragableScenarioController) {
        this.title = title;
        this.body = body;
        this.dragableScenarioController = dragableScenarioController;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        dragableScenarioController.getTextTitle().setText(title);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        dragableScenarioController.getTextBody().setText(body);
    }

    public List<Choice> getOutgoingChoices() {
        return outgoingChoices;
    }

    public void setChoices(List<Choice> choices) {
        this.outgoingChoices = choices;
    }

    public DragableScenarioController getDragableScenarioController() {
        return dragableScenarioController;
    }

    public void setDragableScenarioController(DragableScenarioController dragableScenarioController) {
        this.dragableScenarioController = dragableScenarioController;
    }

    public List<Choice> getIncomingChoices() {
        return incomingChoices;
    }
}
