package com.heiko.textrpgcreator;

import com.heiko.textrpgcreator.controller.node.Controller;
import com.heiko.textrpgcreator.controller.node.DragableScenarioController;
import com.heiko.textrpgcreator.controller.node.WindowController;
import com.heiko.textrpgcreator.controller.ui.DragDropController;
import com.heiko.textrpgcreator.controller.ui.MouseController;
import com.heiko.textrpgcreator.controller.ui.ShortcutController;
import com.heiko.textrpgcreator.scenario.Arrow;
import com.heiko.textrpgcreator.scenario.Scenario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static WindowController windowController;

    private static List<DragableScenarioController> dragableScenarioControllers = new ArrayList<DragableScenarioController>();

    private static List<Scenario> scenarios = new ArrayList<Scenario>();

    private static ShortcutController shortcutController = new ShortcutController();

    private static MouseController mouseController = new MouseController();

    private static DragDropController dragDropController = new DragDropController();

    private static Scenario currentEdit;

    private static List<Node> markedNodes = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Window"));
        scene.getStylesheets().add(App.class.getResource("MainCSS.css").toExternalForm());
        stage.setScene(scene);
        addKeyListeners();
        setMouseListeners();
        stage.show();
    }

    public static void addMarkedNode(Node node, boolean isShiftPressed) {
        if(!isShiftPressed) {
            clearMarkedNodes();
        }
        if(!markedNodes.contains(node)) {
            if(node.toString().contains("coverPane")) {
                markPane(node);
            }
            markedNodes.add(node);
        } else {
            if(node.toString().contains("coverPane")) {
                removeMarkedNode(node, "coverPane");
            }
        }
    }

    private static void markArrow(Arrow a) {
        a.setMarked();
    }

    private static void unmarkArrow(Arrow a) {
        a.removeMarked();
    }

    private static void markPane(Node node) {
        App.getScenarios().forEach((t) -> {
            if(t.getDragableScenarioController().getCoverPane() == node) {
                t.getDragableScenarioController().setMarked();
                App.getDragDropController().getMarkedPaneScenarios().add(t);
            }
        });
    }

    private static void unmarkPane(Node node) {
        App.getScenarios().forEach((t) -> {
            if(t.getDragableScenarioController().getCoverPane() == node) {
                t.getDragableScenarioController().removeMarked();
                App.getDragDropController().getMarkedPaneScenarios().remove(t);
            }
        });
    }

    public static void removeMarkedNode(Node node, String d) {
        markedNodes.remove(node);
        if(d.equals("coverPane")) {
            unmarkPane(node);
        }
    }

    public static void clearMarkedNodes() {
        markedNodes.forEach((t) -> {
            if(t.toString().contains("Line") || t.toString().contains("Circle")) {
                unmarkArrow((Arrow) t);
            } else if(t.toString().contains("coverPane")) {
                unmarkPane(t);
            }
        });
        markedNodes.clear();
    }

    public static void markAllNodes() {
        clearMarkedNodes();
        dragableScenarioControllers.forEach((t) -> {
            markedNodes.add(t.getCoverPane());
            markPane(t.getCoverPane());
        });
    }

    public static Scenario findScenario(AnchorPane pane) {
        scenarios.forEach((t) -> {
            if(t.getDragableScenarioController().getAnchorParentPane() == pane) {
                currentEdit = t;
            }
        });
        return currentEdit;
    }

    private void addKeyListeners() {
        scene.setOnKeyPressed(shortcutController.getKeyPressed());
        scene.setOnKeyReleased(shortcutController.getKeyReleased());
    }

    private void setMouseListeners() {
        scene.setOnScroll(App.getMouseController().getMouseScrolled());
        scene.setOnMousePressed(mouseController.getMousePressed());
        scene.setOnMouseDragged(mouseController.getMouseDragged());
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Controller loadFXMLController(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.load(App.class.getResource(fxml + ".fxml").openStream());
            return fxmlLoader.getController();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static WindowController getWindowController() {
        return windowController;
    }

    public static void setWindowController(WindowController windowController) {
        App.windowController = windowController;
    }

    public static List<DragableScenarioController> getDragableScenarioControllers() {
        return dragableScenarioControllers;
    }

    public static MouseController getMouseController() {
        return mouseController;
    }

    public static ShortcutController getShortcutController() {
        return shortcutController;
    }

    public static List<Scenario> getScenarios() {
        return scenarios;
    }

    public static DragDropController getDragDropController() {
        return dragDropController;
    }

    public static Scenario getCurrentEdit() {
        return currentEdit;
    }

    public static Scene getScene() {
        return scene;
    }

    public static List<Node> getMarkedNodes() {
        return markedNodes;
    }
}
