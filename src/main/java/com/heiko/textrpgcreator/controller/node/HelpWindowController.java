/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.node;

import com.heiko.textrpgcreator.App;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author eiko1
 */
public class HelpWindowController implements Initializable {

    @FXML
    AnchorPane pane;
    @FXML
    TextArea text;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setHelpWindowController(this);
        text.setStyle("-fx-text-fill: white;");
        text.setText("Seperators\n"
                + "	- Tag group seperator ;\n"
                + "	- Tag Seperator :\n"
                + "	- Value Seperator ,\n"
                + "	- Value Grouping ()\n"
                + "\n"
                + "- Tags\n"
                + "	- Start\n"
                + "		- No special requirement (Start)\n"
                + "		- Class (Start:Class(Warrior,Mage))\n"
                + "		- Level (Start:Level(Above(10):Below(20)))\n"
                + "		- Race (Start:Race(Human,Wolf))\n"
                + "		- Gender (Start:Gender(Male,Female))\n"
                + "		Combined example: Start:Class(Mage):Gender(Female);\n"
                + "	- End\n"
                + "	- Encounter\n"
                + "		- Random\n"
                + "			- Difficulty (Encounter:Random:Difficulty(10))\n"
                + "			- Enemey Count (Encounter:Random:EnemyCount(4))\n"
                + "			Combined example: Encounter:Random:Difficulty(10):EnemyCount(4);\n"
                + "		- Predefined\n"
                + "			- Defined Enemy (Encounter:Defined(Geralt the slayer))\n"
                + "		Combined example: Encounter:Random:Difficulty(10):EnemyCount(4)\n"
                + "	- Requirement for Choices\n"
                + "		- Has (Requirement:Has(Knife,Stick))\n"
                + "		- Wears (Requirement:Wears(Nothing,Clothes))\n"
                + "		- Hp (Requirement:Hp(Below(50),Above(10)))\n"
                + "		- Is (Requirement:Is(Dirty,Clean))\n"
                + "		- Class (Start:Class(Warrior,Mage))\n"
                + "		- Level (Start:Level(Above(10):Below(20)))\n"
                + "		- Race (Start:Race(Human,Wolf))\n"
                + "		- Gender (Start:Gender(Male,Female))\n"
                + "		Combined example: Requirement:Is(Clean,Wet):Has(Wooden Stick,Club);\n"
                + "	- Can not go back (NoBack)\n"
                + "	\n"
                + "Combined example: Requirement:Has(Knife):Wears(Nothing):Level(Above(5)):Gender(male);Encounter:Random:Difficulty(15):EnemyCount(5);");
    }

    public AnchorPane getPane() {
        return pane;
    }

    public TextArea getText() {
        return text;
    }
}
