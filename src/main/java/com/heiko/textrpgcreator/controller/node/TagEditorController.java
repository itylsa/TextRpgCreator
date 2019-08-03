/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import com.heiko.textrpgcreator.scenario.Tag;
import com.heiko.textrpgcreator.scenario.TagList;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class TagEditorController extends Controller implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private ComboBox groupCb;
    @FXML
    private FlowPane choicePane;
    @FXML
    private VBox tagsBox;
    @FXML
    private ScrollPane tagsScrollPane;

    private Scenario scenario;

    private Choice choice;

    private List<TagController> tagsList = new ArrayList<>();

    private List<ComboBox> cbs = new ArrayList<>();

    private TagList tagList = new TagList();

    public void initialize(URL url, ResourceBundle rb) {
        App.setTagEditorController(this);
        tagsScrollPane.heightProperty().addListener((o, oldValue, newValue) -> {
            tagsBox.setPrefHeight(newValue.doubleValue() - 2);
        });
        tagsScrollPane.widthProperty().addListener((o, oldValue, newValue) -> {
            tagsBox.setPrefWidth(newValue.doubleValue() - 20);
        });
    }

    public void openTagEditor(Scenario scenario, Choice choice) {
        if(scenario != null) {
            this.scenario = scenario;
            groupCb.setItems(App.getFileController().getGroupTags("Scenario"));
            groupCb.setValue(groupCb.getItems().get(0));
        } else {
            this.choice = choice;
            groupCb.setItems(App.getFileController().getGroupTags("Choice"));
            groupCb.setValue(groupCb.getItems().get(0));
        }
        tagList.setList(App.getFileController().getTags(groupCb.getValue().toString()));
        addThings("");
        groupCb.setOnAction((e) -> {
            tagList.setList(App.getFileController().getTags(groupCb.getValue().toString()));
            choicePane.getChildren().clear();
            addThings("");
        });
        loadTags();
    }

    public void closeTagEditor() {
        App.closeEditor();
        String tags = "";
        for(TagController tc : tagsList) {
            tags = tags + tc.getGroupLabel().getText() + tc.getTagLabel().getText() + tc.getValueLabel().getText() + ";";
        }
        if(scenario != null) {
            App.openEditor("ScenarioEditor", scenario, null);
            App.getScenarioEditorController().setTags(tags);
        } else {
            App.openEditor("ChoiceEditor", null, choice);
            App.getChoiceEditorController().setTags(tags);
        }
    }

    public void addThings(String tagName) {
        ObservableList list = tagList.getObservableList(tagName);
        if(list == null) {
            //Nothing
        } else if(list.get(0) == null) {
            list.set(0, "");
            addValueComboBox(list);
        } else if(list.get(0).equals("novalues")) {
            addValueField();
        } else {
            addComboBox(list);
        }
    }

    public void addComboBox(ObservableList list) {
        ComboBox cb = new ComboBox();
        cb.setItems(list);
        cb.setOnAction((e) -> {
//            tagList.setList(App.getFileController().getTags(groupCb.getValue().toString()));
            int a = choicePane.getChildren().size() - choicePane.getChildren().indexOf(cb) - 1;
            for(int i = 0; i < a; i++) {
                choicePane.getChildren().remove(choicePane.getChildren().size() - 1);
            }
            addThings(cb.getValue().toString());
        });
        choicePane.getChildren().add(cb);
    }

    public void addValueComboBox(ObservableList list) {
        ComboBox cb = new ComboBox();
        cb.setStyle("-fx-background-color: red;");
        cb.setItems(FXCollections.observableArrayList(list));
        choicePane.getChildren().add(cb);
        cb.setOnAction((e) -> {
            ObservableList items = FXCollections.observableArrayList(cb.getItems());
            items.remove(cb.getValue().toString());
            if(checkForEmptyValueComboBoxes() == 0 && items.size() > 1) {
                addValueComboBox(cb.getItems());
            } else if(checkForEmptyValueComboBoxes() > 1) {
                removeLastEmptyValueComboBox();
            }
            updateChoosableValues();
        });
    }

    public void addValueField() {
        TextField tf = new TextField();
        choicePane.getChildren().add(tf);
    }

    private void updateChoosableValues() {
        List<ComboBox> cbs = getAllValueComboBoxes();
        ComboBox t = (ComboBox) choicePane.getChildren().get(choicePane.getChildren().size() - cbs.size() - 1);
        ObservableList<String> allItems = tagList.getObservableList(t.getValue().toString());
        allItems.remove(null);
        ObservableList<String> itemsToRemove = FXCollections.observableArrayList();
        ObservableList<String> itemsToAdd = FXCollections.observableArrayList();
        for(ComboBox c : cbs) {
            if(c.getValue() != null && !c.getValue().toString().equals("")) {
                itemsToRemove.add(c.getValue().toString());
            }
        }
        itemsToAdd.addAll(allItems);
        for(String i : allItems) {
            for(ComboBox c : cbs) {
                if(c.getValue() != null && i.equals(c.getValue().toString())) {
                    itemsToAdd.remove(c.getValue().toString());
                }
            }
        }
        if(!itemsToRemove.isEmpty()) {
            removeItemsFromValueComboBoxes(cbs, itemsToRemove);
        }
        if(!itemsToAdd.isEmpty()) {
            addItemsToValueComboBoxes(cbs, itemsToAdd);
        }
    }

    private void removeItemsFromValueComboBoxes(List<ComboBox> cbs, List<String> itemsToRemove) {
        for(ComboBox c : cbs) {
            for(String i : itemsToRemove) {
                if(c.getValue() == null || !c.getValue().toString().equals(i)) {
                    c.getItems().remove(i);
                }
            }
        }
    }

    private void addItemsToValueComboBoxes(List<ComboBox> cbs, List<String> itemsToAdd) {
        for(ComboBox c : cbs) {
            for(String i : itemsToAdd) {
                if(!c.getItems().contains(i)) {
                    c.getItems().add(i);
                }
            }
        }
    }

    public int checkForEmptyValueComboBoxes() {
        List<ComboBox> list = getAllValueComboBoxes();
        int t = 0;
        for(ComboBox c : list) {
            if(c.getValue() == null || c.getValue().toString().equals("")) {
                t++;
            }
        }
        return t;
    }

    public List<ComboBox> getAllValueComboBoxes() {
        List<ComboBox> list = new ArrayList<>();
        int a = choicePane.getChildren().size();
        for(int i = 0; i < a; i++) {
            if(choicePane.getChildren().get(i).getStyle().equals("-fx-background-color: red;")) {
                list.add((ComboBox) choicePane.getChildren().get(i));
            }
        }
        return list;
    }

    public List<ComboBox> getAllTagComboBoxes() {
        List<ComboBox> list = new ArrayList<>();
        int a = choicePane.getChildren().size();
        for(int i = 0; i < a; i++) {
            if(choicePane.getChildren().get(i).getStyle() == null || choicePane.getChildren().get(i).getStyle().equals("")) {
                list.add((ComboBox) choicePane.getChildren().get(i));
            }
        }
        return list;
    }

    public void removeLastEmptyValueComboBox() {
        List<ComboBox> list = getAllValueComboBoxes();
        ComboBox box = null;
        for(ComboBox c : list) {
            if(c.getValue() == null || c.getValue().toString().equals("")) {
                box = c;
            }
        }
        choicePane.getChildren().remove(box);
    }

    private void loadTags() {
        String tags = "";
        if(scenario != null) {
            tags = scenario.getTags();
        } else {
            tags = choice.getTags();
        }
        if(tags != null && !tags.isEmpty()) {
            List<String> tl = Arrays.asList(tags.split("\\;"));
            for(String s : tl) {
                TagController tc = (TagController) App.loadFXMLController("Tag");
                List<String> l = Arrays.asList(s.split("\\:"));
                if(l.size() > 0 && !l.get(0).equals("")) {
                    tc.setGroupLabel(l.get(0) + ":");
                } else {
                    tc.setGroupLabel("");
                }
                if(l.size() > 1 && !l.get(1).equals("")) {
                    tc.setTagLabel(l.get(1) + ":");
                } else {
                    tc.setTagLabel("");
                }
                if(l.size() > 2 && !l.get(2).equals("")) {
                    tc.setValueLabel(l.get(2));
                } else {
                    tc.setValueLabel("");
                }
                tagsList.add(tc);
                tagsBox.getChildren().add(tc.getAnchorParentPane());
            }
        }
    }

    @FXML
    public void saveTag() {
        TagController tc = (TagController) App.loadFXMLController("Tag");
        tc.setGroupLabel(groupCb.getValue().toString() + ":");
        List<ComboBox> tcbs = getAllTagComboBoxes();
        String s = "";
        for(int i = 0; i < tcbs.size(); i++) {
            ComboBox c = tcbs.get(i);
            if(c.getValue() != null) {
                s = s + c.getValue().toString();
                if(i != tcbs.size() - 1) {
                    s = s + ">";
                }
            }
        }
        s = s + ":";
        tc.setTagLabel(s);
        List<ComboBox> vcbs = getAllValueComboBoxes();
        s = "";
        for(int i = 0; i < vcbs.size(); i++) {
            ComboBox c = vcbs.get(i);
            if(c.getValue() != null) {
                s = s + c.getValue().toString();
                if(i != vcbs.size() - 1 && !c.getValue().toString().equals("")) {
                    s = s + ",";
                }
            }
        }
        if(s.endsWith(",")) {
            s = s.substring(0, s.lastIndexOf(","));
        }
        tc.setValueLabel(s);
        tagsBox.getChildren().add(tc.getAnchorParentPane());
        tagsList.add(tc);
    }

    public Parent getPane() {
        return pane;
    }

    public AnchorPane getAnchorParentPane() {
        return (AnchorPane) pane;
    }

    public VBox getTagsBox() {
        return tagsBox;
    }

    public ComboBox getGroupCb() {
        return groupCb;
    }

    public Pane getChoicePane() {
        return choicePane;
    }

    public ScrollPane getTagsScrollPane() {
        return tagsScrollPane;
    }

    public List<TagController> getTagsList() {
        return tagsList;
    }
}
