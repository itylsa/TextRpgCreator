/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.scenario;

/**
 *
 * @author eiko1
 */
public class Choice {

    private int id;

    private String text;

    private Scenario startScenario;

    private Scenario endScenario;

    private Arrow choiceArrow;

    public Choice(int id, String text, Scenario startScenario, Scenario endScenario, Arrow choiceArrow) {
        this.id = id;
        this.text = text;
        this.startScenario = startScenario;
        this.endScenario = endScenario;
        this.choiceArrow = choiceArrow;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Scenario getStartScenario() {
        return startScenario;
    }

    public void setStartScenario(Scenario startScenario) {
        this.startScenario = startScenario;
    }

    public Scenario getEndScenario() {
        return endScenario;
    }

    public void setEndScenario(Scenario endScenario) {
        this.endScenario = endScenario;
    }

    public Arrow getChoiceArrow() {
        return choiceArrow;
    }

    public void setChoiceArrow(Arrow choiceArrow) {
        this.choiceArrow = choiceArrow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
