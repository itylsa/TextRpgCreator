/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator;

import com.heiko.textrpgcreator.controller.node.InfoController;
import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import com.heiko.textrpgcreator.scenario.Tag;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.util.Duration;
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

    private File scenarioGroupFile = new File("Assets\\ScenarioTags.txt");

    private File choiceGroupFile = new File("Assets\\ChoiceTags.txt");

    private File startFile = new File("Assets\\StartTags.txt");

    private File encounterFile = new File("Assets\\EncounterTags.txt");

    private File requirementFile = new File("Assets\\RequirementTags.txt");

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
                fos.flush();
                fos.close();

                App.showInfoBox("Progress saved!");
                App.startAutosave();
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
            App.showInfoBox("Progress loaded!");
            App.startAutosave();
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

    public ObservableList getGroupTags(String file) {
        file = file.toLowerCase();
        ObservableList list = FXCollections.observableArrayList();
        switch(file) {
            case "scenario":
                list = readLines(scenarioGroupFile);
                break;
            case "choice":
                list = readLines(choiceGroupFile);
                break;
        }
        return list;
    }

    public List<Tag> getTags(String file) {
        file = file.toLowerCase();
        List<Tag> tags = new ArrayList<>();
        switch(file) {
            case "encounter":
                getItems(readLines(encounterFile), tags);
                break;
            case "requirement":
                getItems(readLines(requirementFile), tags);
                break;
            case "start":
                getItems(readLines(startFile), tags);
                break;
            default:
                System.out.println("Nothing here");
        }
        return tags;
    }

    private List<Tag> getItems(ObservableList list, List<Tag> tags) {
        for(Object l : list) {
            String line = l.toString();
            if(line.contains("-")) {
                tags.set(tags.size() - 1, addSubTags(line, tags.get(tags.size() - 1)));
            } else if(line.contains("(")) {
                tags.add(addValues(line, null));
            } else {
                tags.add(getNewTag(line));
            }
        }
//        for(Tag tag : tags) {
//            tt(tag, 0);
//        }
        return tags;
    }

    private void tt(Tag t, int i) {
        System.out.println("Level " + i);
        System.out.println("Tag Name " + t.getTagName());
        System.out.println("SubTags " + t.getSubTags());
        if(t.getValues() != null && !t.getValues().isEmpty()) {
            System.out.println("Values " + t.getValues());
        }
        if(!t.getSubTags().isEmpty()) {
            i++;
            for(Tag tt : t.getSubTags()) {
                tt(tt, i);
            }
        }
    }

    private Tag getNewTag(String line) {
        Tag tag = new Tag();
        tag.setTagName(line);
        return tag;
    }

    private Tag addSubTags(String line, Tag tag) {
        Tag ot = tag;
        int n = line.substring(0, line.lastIndexOf("-")).length();
        for(int i = 0; i < n; i++) {
            tag = tag.getSubTags().get(tag.getSubTags().size() - 1);
        }
        line = line.substring(line.lastIndexOf("-") + 1);
        Tag t = new Tag();
        if(line.contains("(")) {
            t = addValues(line, t);
        } else {
            t = getNewTag(line);
        }
        t.setParent(tag);
        if(ot != tag) {
            tag.getSubTags().add(t);
            for(int i = 0; i < n; i++) {
                tag = tag.getParent();
            }
            ot = tag;
        } else {
            ot.getSubTags().add(t);
        }
        return ot;
    }

    private Tag addValues(String line, Tag tag) {
        String oldLine = line;
        if(!line.startsWith("(")) {
            tag = getNewTag(line.split("\\(")[0]);
            line = line.substring(line.indexOf("("));
        }
        if(line.length() > 2) {
            if(line.contains(",")) {
                List<String> l = Arrays.asList(line.split("\\,"));
                List<String> values = new ArrayList<>();
                for(String s : l) {
                    values.addAll(readValues(s.replaceAll("[\\(\\)\\,]", "")));
                }
                tag.setAllValues(values);
            } else {
                tag.setAllValues(readValues(line.replaceAll("[\\(\\)\\,]", "")));
            }
        } else if(line.length() == 2) {
            tag.setAllValues(Arrays.asList("novalues"));
        } else {
            tag.setAllValues(Arrays.asList(""));
        }
//        if(line.length() > 2 && line.contains(",")) {
//            line = line.split("\\(")[1].split("\\)")[0];
//            tag.setAllValues(Arrays.asList(line.split("\\,")));
//        } else if(line.length() > 2 && !line.contains(",")) {
//            line = line.split("\\(")[1].split("\\)")[0];
//            tag.setAllValues(Arrays.asList(line));
//        } else if(line.length() == 2) {
//            tag.setAllValues(Arrays.asList("novalues"));
//        } else {
//            tag.setAllValues(Arrays.asList(""));
//        }
        return tag;
    }

    private List<String> readValues(String name) {
        File file = new File("Assets/Values/" + name + ".xml");
        if(!file.exists()) {
            return Arrays.asList("");
        }
        List<String> valueList = new ArrayList<>();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(file);
            Element list = document.getRootElement();
            String type = list.getChildText("type");

            switch(type) {
                case "simple":
                    valueList = readSimpleValues(list, valueList);
                    break;
                case "item":
                    break;
            }

//            App.setAdventureName(list.getChildText("Name"));
//            App.setHighestId(Integer.parseInt(list.getChildText("HighestId")));
//
//            List<Element> scenarios = list.getChild("Scenarios").getChildren();
//            List<Element> choices = new ArrayList<>();
//            for(Element s : scenarios) {
//                Scenario scenario = new Scenario(Integer.valueOf(s.getChildText("Id")), s.getChildText("Tags"), s.getChildText("Text"), null);
//                App.setHighestId(scenario.getId());
//
//                App.getWindowController().addDragableScenario(
//                        Double.valueOf(s.getChild("Position").getChildText("X")),
//                        Double.valueOf(s.getChild("Position").getChildText("Y")),
//                        scenario);
//
//                for(Element choice : s.getChild("Choices").getChildren("Choice")) {
//                    choices.add(choice);
//                }
//            }
//            for(Element c : choices) {
//                Scenario start = App.findScenario(Integer.valueOf(c.getParentElement().getParentElement().getChildText("Id")));
//                Scenario end = App.findScenario(Integer.valueOf(c.getChildText("Id")));
//                App.getWindowController().addArrow(start, end, Integer.valueOf(c.getChildText("Id")), c.getChildText("Tags"), c.getChildText("Text"));
//            }
//            App.showInfoBox("Progress loaded!");
//            App.startAutosave();
        } catch(JDOMException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valueList;
    }

    private List<String> readSimpleValues(Element list, List<String> valueList) {
        List<Element> items = list.getChild("items").getChildren("item");
        for(Element item : items) {
            valueList.add(item.getChildText("name"));
        }
        return valueList;
    }

    private ObservableList readLines(File file) {
        ObservableList list = FXCollections.observableArrayList();
        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader(file));
            r.lines().forEach((t) -> {
                list.add(t);
            });
        } catch(FileNotFoundException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
