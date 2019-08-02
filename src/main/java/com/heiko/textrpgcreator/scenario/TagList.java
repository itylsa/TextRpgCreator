/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.scenario;

import com.heiko.textrpgcreator.App;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author eiko1
 */
public class TagList {

    private List<Tag> list = new ArrayList<>();

    public ObservableList getObservableList(String tagName) {
        ObservableList<String> list = FXCollections.observableArrayList();
        Tag tag = new Tag();
        if(!tagName.equals("")) {
            tag = getTag(tagName);
            for(Tag t : tag.getSubTags()) {
                list.add(t.getTagName());
            }
        } else {
            for(Tag t : this.list) {
                list.add(t.getTagName());
            }
        }
        if(list.isEmpty() && tag.getValues() == null) {
            return null;
        } else if(list.isEmpty() && tag.getValues().get(0).equals("novalues")) {
            list.add("novalues");
            return list;
        } else if(list.isEmpty() && !tag.getValues().isEmpty()) {
            list = tag.getValues();
            list.add(0, null);
            return list;
        }
        return list;
    }

    private Tag getTag(String tagName) {
        Tag tag = new Tag();
        for(Tag t : list) {
            if(t.getTagName().equals(tagName)) {
                tag = t;
                return tag;
            }
            if(!t.getSubTags().isEmpty()) {
                tag = searchSubTag(t, tagName);
            }
        }
        return tag;
    }

    private Tag searchSubTag(Tag t, String tagName) {
        Tag tag = new Tag();
        for(Tag tt : t.getSubTags()) {
            if(tt.getTagName().equals(tagName)) {
                tag = tt;
                return tag;
            }
            if(!tt.getSubTags().isEmpty()) {
                tag = searchSubTag(tt, tagName);
            }
        }
        return tag;
    }

    public int getSize() {
        return list.size();
    }

    public List<Tag> getList() {
        return list;
    }

    public void setList(List<Tag> list) {
        this.list = list;
    }
}
