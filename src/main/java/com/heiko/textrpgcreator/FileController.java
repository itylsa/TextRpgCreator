/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator;

import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author eiko1
 */
public class FileController {

    public void saveProgress(File file) {
        if(!file.exists()) {
            try {
                Element adventure = new Element("Adventure");
                adventure.addContent(new Element("Name").setText("TODO"));

                Element highestId = new Element("HighestId").setText(String.valueOf(App.getHighestId()));
                adventure.addContent(highestId);

                Element scenarios = new Element("Scenarios");
                adventure.addContent(scenarios);

                for(Scenario s : App.getScenarios()) {
                    Element scenario = new Element("Scenario");
                    scenarios.addContent(scenario);

                    Element id = new Element("Id").setText(String.valueOf(s.getId()));
                    scenario.addContent(id);

                    Element tags = new Element("Tags").setText(s.getTags());
                    scenario.addContent(tags);

                    Element text = new Element("Text").setText(s.getBody());
                    scenario.addContent(text);

                    Element position = new Element("Position");
                    scenario.addContent(position);

                    Element x = new Element("X").setText(String.valueOf(s.getDragableScenarioController().getAnchorParentPane().getLayoutX()));
                    position.addContent(x);

                    Element y = new Element("Y").setText(String.valueOf(s.getDragableScenarioController().getAnchorParentPane().getLayoutY()));
                    position.addContent(y);

                    Element choices = new Element("Choices");
                    scenario.addContent(choices);

                    for(Choice c : s.getOutgoingChoices()) {
                        Element choice = new Element("Choice");
                        choices.addContent(choice);

                        Element choiceId = new Element("Id").setText(String.valueOf(c.getId()));
                        choice.addContent(choiceId);

                        Element choiceText = new Element("Text").setText(c.getText());
                        choice.addContent(choiceText);
                    }
                }

                XMLOutputter xmlOutput = new XMLOutputter();

                xmlOutput.setFormat(Format.getPrettyFormat());
                FileOutputStream fos = new FileOutputStream(file);
                xmlOutput.output(adventure, fos);
                fos.close();
            } catch(IOException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            file.delete();
            saveProgress(file);
        }
    }

    public void loadProgress() {
        //TODO
    }
}
