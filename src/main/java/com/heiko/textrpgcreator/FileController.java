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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
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
                adventure.addContent(new Element("Name").setText(App.getAdventureName()));

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

                        Element choiceTags = new Element("Tags").setText(c.getTags());
                        choice.addContent(choiceTags);

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

    public void loadProgress(File file) {
        //TODO
        App.clearAll();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(file);
            Element adventure = document.getRootElement();
            App.setAdventureName(adventure.getChildText("Name"));
            App.setHighestId(Integer.parseInt(adventure.getChildText("HighestId")));

            List<Element> scenarios = adventure.getChild("Scenarios").getChildren();
            List<Element> choices = new ArrayList<>();
            for(Element s : scenarios) {
                Scenario scenario = new Scenario(Integer.valueOf(s.getChildText("Id")), s.getChildText("Tags"), s.getChildText("Text"), null);
                App.setHighestId(scenario.getId());

                App.getWindowController().addDragableScenario(
                        Double.valueOf(s.getChild("Position").getChildText("X")),
                        Double.valueOf(s.getChild("Position").getChildText("Y")),
                        scenario);

                for(Element choice : s.getChild("Choices").getChildren("Choice")) {
                    choices.add(choice);
                }
            }
            for(Element c : choices) {
                Scenario start = App.findScenario(Integer.valueOf(c.getParentElement().getParentElement().getChildText("Id")));
                Scenario end = App.findScenario(Integer.valueOf(c.getChildText("Id")));
                App.getWindowController().addArrow(start, end, Integer.valueOf(c.getChildText("Id")), c.getChildText("Tags"), c.getChildText("Text"));
            }

        } catch(JDOMException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getInitialPath() {
        if(!new File("Settings.xml").exists()) {
            setInitialPath();
        }
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build("Settings.xml");
            Element settings = document.getRootElement();
            App.setInitialPath(settings.getChildText("InitialPath"));
        } catch(JDOMException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setInitialPath() {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document;
            if(new File("Settings.xml").exists()) {
                document = (Document) builder.build("Settings.xml");
                document.getRootElement().getChild("InitialPath").setText(App.getInitialPath());
            } else {
                Element settings = new Element("Settings");
                document = new Document(settings);
                settings.addContent(new Element("InitialPath").setText(App.getInitialPath()));
            }

            XMLOutputter xmlOutput = new XMLOutputter();

            xmlOutput.setFormat(Format.getPrettyFormat());
            FileOutputStream fos = new FileOutputStream("Settings.xml");
            xmlOutput.output(document, fos);
            fos.close();
        } catch(IOException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch(JDOMException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
