/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.scenario;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author eiko1
 */
public class Tag {

    private String tagName;
    
    private Tag parent;

    private ObservableList<Tag> subTags = FXCollections.observableArrayList();

    private ObservableList<String> values;
    
    public ObservableList<String> getAllSubTagNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for(Tag t : subTags) {
            list.add(t.getTagName());
        }
        return list;
    }
    
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public ObservableList<Tag> getSubTags() {
        return subTags;
    }

    public ObservableList<String> getValues() {
        return values;
    }

    public void setAllValues(List<String> values) {
        this.values = FXCollections.observableArrayList(values);
    }
    
    public void setAllSubTags(List<Tag> tags) {
        this.subTags = FXCollections.observableArrayList(tags);
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }
}
