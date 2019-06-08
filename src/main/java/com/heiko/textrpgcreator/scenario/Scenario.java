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

    private int id;

    private String tags;

    private String body;

    private List<Choice> outgoingChoices = new ArrayList<Choice>();

    private List<Choice> incomingChoices = new ArrayList<Choice>();

    private DragableScenarioController dragableScenarioController;

    public Scenario(int id, String tags, String body, DragableScenarioController dragableScenarioController) {
        this.id = id;
        this.tags = tags;
        this.body = body;
        this.dragableScenarioController = dragableScenarioController;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
        dragableScenarioController.getTextTags().setText(tags);
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

    public DragableScenarioController getDragableScenarioController() {
        return dragableScenarioController;
    }

    public void setDragableScenarioController(DragableScenarioController dragableScenarioController) {
        this.dragableScenarioController = dragableScenarioController;
    }

    public List<Choice> getIncomingChoices() {
        return incomingChoices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
