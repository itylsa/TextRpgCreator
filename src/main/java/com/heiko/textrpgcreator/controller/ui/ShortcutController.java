/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiko.textrpgcreator.controller.ui;

import com.heiko.textrpgcreator.App;
import java.awt.Event;
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

/**
 *
 * @author eiko1
 */
public class ShortcutController {

    private boolean isControlPressed = false;

    private boolean isShiftPressed = false;

    private EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent e) {
            //If control is pressed
            if(e.getCode().toString().equals("CONTROL")) {
                isControlPressed = true;
            }
            if(e.getCode().toString().equals("SHIFT")) {
                isShiftPressed = true;
            }
            //If control and s are pressed
            if(e.getCode().toString().equals("S") && isControlPressed) {
                System.out.println("SAVE");
                App.getFileController().saveProgress(new File("C:\\Users\\eiko1\\Desktop\\TEST\\" + "testfile" + ".xml"));
                //TODO
            }
            if(e.getCode().toString().equals("A") && isControlPressed) {
                App.markAllNodes();
            }
            if(e.getCode().toString().equals("DELETE")) {
                App.deleteMarkedScenarios();
                e.consume();
            }
            if(e.getCode().toString().equals("ESCAPE")) {
                if(App.getEditorStage() != null) {
                    App.getEditorStage().fireEvent(new WindowEvent(App.getEditorStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
                    e.consume();
                }
            }
        }
    };

    private EventHandler<KeyEvent> escPressed = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent e) {
            if(e.getCode().toString().equals("ESCAPE")) {
                if(App.getEditorStage() != null) {
                    App.getEditorStage().fireEvent(new WindowEvent(App.getEditorStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
                    e.consume();
                }
            }
        }
    };

    private EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent e) {
            //If control is released
            if(e.getCode().toString().equals("CONTROL")) {
                isControlPressed = false;
            }
            if(e.getCode().toString().equals("SHIFT")) {
                isShiftPressed = false;
            }
        }
    };

    public EventHandler<KeyEvent> getKeyPressed() {
        return keyPressed;
    }

    public EventHandler<KeyEvent> getKeyReleased() {
        return keyReleased;
    }

    public boolean isIsControlPressed() {
        return isControlPressed;
    }

    public boolean isIsShiftPressed() {
        return isShiftPressed;
    }

    public void setIsControlPressed(boolean isControlPressed) {
        this.isControlPressed = isControlPressed;
    }

    public void setIsShiftPressed(boolean isShiftPressed) {
        this.isShiftPressed = isShiftPressed;
    }

    public EventHandler<KeyEvent> getEscPressed() {
        return escPressed;
    }
}
