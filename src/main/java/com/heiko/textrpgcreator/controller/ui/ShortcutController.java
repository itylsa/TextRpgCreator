/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.ui;

import com.heiko.textrpgcreator.App;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author eiko1
 */
public class ShortcutController {

    private boolean isControlPressed = false;

    private EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent e) {
            //If control is pressed
            if(e.getCode().toString().equals("CONTROL")) {
                isControlPressed = true;
            }
            //If control and s are pressed
            if(e.getCode().toString().equals("S") && isControlPressed) {
                System.out.println("SAVE");
            }
        }
    };

    private EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent e) {
            //If control is released
            if(e.getCode().toString().equals("CONTROL")) {
                isControlPressed = false;
            }
        }
    };

    public EventHandler<KeyEvent> getKeyPressed() {
        return keyPressed;
    }

    public EventHandler<KeyEvent> getKeyReleased() {
        return keyReleased;
    }
}
